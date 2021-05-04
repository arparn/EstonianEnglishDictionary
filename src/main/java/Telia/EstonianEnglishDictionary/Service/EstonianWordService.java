package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EstonianWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstonianWordService {

    @Autowired
    private EnglishWordService englishWordService;

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
        englishWordService.addWord(translation, word);
        return estWord;
    }

    public List<String> translate(String word) {
        List<EstonianWord> similarWords =  estonianWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().toLowerCase().equals(word.toLowerCase()))
                .collect(Collectors.toList());
        List<String> translations = new LinkedList<>();
        similarWords.forEach(w -> w.getEquivalents().forEach(t -> translations.add(t.getWord())));
        return translations;
    }
}
