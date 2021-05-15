package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Algorithm.LevenshteinAlgorithm;
import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnglishWordService {

    private static final LevenshteinAlgorithm levenshteinAlgorithm = new LevenshteinAlgorithm();

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private EnglishWordsRepository englishWordsRepository;

    public List<EnglishWord> getAll() {
        return englishWordsRepository.findAll();
    }

    public boolean addWord(String word, String translation) {
        if (word.equals("") || translation.equals("")) {
            return false;
        }
        boolean added = true;
        Translation translationObj = new Translation(translation);
        Optional<EnglishWord> enWordOpt = englishWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .findFirst();
        if (enWordOpt.isPresent()) {
            added = false;
            EnglishWord enWord = enWordOpt.get();
            if (enWord.getEquivalents().stream().noneMatch(x -> x.getWord().equals(translation))) {
                translationRepository.save(translationObj);
                enWord.getEquivalents().add(translationObj);
                englishWordsRepository.save(enWord);
                added = true;
            }
            return added;
        }
        translationRepository.save(translationObj);
        EnglishWord enWord = new EnglishWord(word, translationObj);
        englishWordsRepository.save(enWord);
        return added;
    }

    public Map<String, Object> translate(String word) {
        Map<String, Object> words = new LinkedHashMap<>();
        if (word.length() < 1) {
            return null;
        }
        boolean foundExact = true;
        List<EnglishWord> suitableWords =  englishWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .collect(Collectors.toList());
        if (suitableWords.size() == 0) {
            foundExact = false;
            suitableWords =  englishWordsRepository.findAll()
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
        if (englishWordsRepository.findById(id).isPresent()) {
            englishWordsRepository.deleteById(id);
        }
    }

    public void deleteTranslation(Long id, Long translationId) {
        Optional<EnglishWord> enWordObj = englishWordsRepository.findById(id);
        Optional<Translation> translationObj = translationRepository.findById(translationId);
        if (enWordObj.isPresent()) {
            EnglishWord enWord = enWordObj.get();
            if (enWord.getEquivalents().size() > 1 && translationObj.isPresent()) {
                Translation translation = translationObj.get();
                enWord.getEquivalents().remove(translation);
                translationRepository.deleteById(translationId);
            }
        }
    }
}
