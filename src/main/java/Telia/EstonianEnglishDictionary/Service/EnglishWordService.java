package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnglishWordService {

    @Autowired
    private EstonianWordService estonianWordService;

    @Autowired
    private TranslationRepository translationRepository;

    @Autowired
    private EnglishWordsRepository englishWordsRepository;

    public List<EnglishWord> getAll() {
        return englishWordsRepository.findAll();
    }

    public EnglishWord addWord(String word, String translation) {
        Translation translationObj = new Translation(translation);
        Optional<EnglishWord> enWordOpt = englishWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .findFirst();
        if (enWordOpt.isPresent()) {
            EnglishWord enWord = enWordOpt.get();
            if (enWord.getEquivalents().stream().noneMatch(x -> x.getWord().equals(translation))) {
                translationRepository.save(translationObj);
                enWord.getEquivalents().add(translationObj);
                englishWordsRepository.save(enWord);
                estonianWordService.addWord(translation, word);
            }
            return enWord;
        }
        translationRepository.save(translationObj);
        EnglishWord enWord = new EnglishWord(word, translationObj);
        englishWordsRepository.save(enWord);
        estonianWordService.addWord(translation, word);
        return enWord;
    }

    public List<Translation> translate(String word) {
        List<EnglishWord> similarWords =  englishWordsRepository.findAll()
                .stream()
                .filter(x -> x.getWord().equals(word))
                .collect(Collectors.toList());
        List<Translation> translations = new LinkedList<>();
        similarWords.forEach(w -> translations.addAll(w.getEquivalents()));
        return translations;
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
