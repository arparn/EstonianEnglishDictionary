package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Algorithm.LevenshteinAlgorithm;
import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EstonianWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstonianWordService {

    private static final LevenshteinAlgorithm levenshteinAlgorithm = new LevenshteinAlgorithm();

    @Autowired
    private EstonianWordsRepository estonianWordsRepository;

    @Autowired
    private TranslationRepository translationRepository;

    public List<EstonianWord> getAll() {
        return estonianWordsRepository.findAll();
    }

    public EstonianWord addWord(String word, String translation) {
        Translation translationObj = new Translation(translation);
        Optional<EstonianWord> estWordOpt = estonianWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .findFirst();
        if (estWordOpt.isPresent()) {
            EstonianWord estWord = estWordOpt.get();
            if (estWord.getEquivalents().stream().noneMatch(x -> x.getWord().equals(translation))) {
                translationRepository.save(translationObj);
                estWord.getEquivalents().add(translationObj);
                estonianWordsRepository.save(estWord);
            }
            return estWord;
        }
        translationRepository.save(translationObj);
        EstonianWord estWord = new EstonianWord(word, translationObj);
        estonianWordsRepository.save(estWord);
        return estWord;
    }

    public Map<String, Object> translate(String word) {
        Map<String, Object> words = new LinkedHashMap<>();
        if (word.length() < 1) {
            return null;
        }
        boolean foundExact = true;
        List<EstonianWord> suitableWords =  estonianWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .collect(Collectors.toList());
        if (suitableWords.size() == 0) {
            foundExact = false;
            suitableWords =  estonianWordsRepository.findAll()
                    .stream()
                    .filter(x -> levenshteinAlgorithm.calculateLevenshtein(word, x.getWord()) <= 2)
                    .collect(Collectors.toList());
        }
        //List<Translation> translations = new LinkedList<>();
        //similarWords.forEach(w -> translations.addAll(w.getEquivalents()));
        words.put("wordList", suitableWords);
        words.put("foundExact", foundExact);
        return words;
    }

    public void deleteWordAndTranslations(Long id) {
        if (estonianWordsRepository.findById(id).isPresent()) {
            estonianWordsRepository.deleteById(id);
        }
    }

    public void deleteTranslation(Long id, Long translationId) {
        Optional<EstonianWord> estWordObj = estonianWordsRepository.findById(id);
        Optional<Translation> translationObj = translationRepository.findById(translationId);
        if (estWordObj.isPresent()) {
            EstonianWord estWord = estWordObj.get();
            if (estWord.getEquivalents().size() > 1 && translationObj.isPresent()) {
                Translation translation = translationObj.get();
                estWord.getEquivalents().remove(translation);
                translationRepository.deleteById(translationId);
            }
        }
    }
}
