package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnglishWordService {

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
            }
            return enWord;
        }
        translationRepository.save(translationObj);
        EnglishWord enWord = new EnglishWord(word, translationObj);
        englishWordsRepository.save(enWord);
        return enWord;
    }

    public static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    public static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

    public int calculateLevenshtein(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1] + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                                            dp[i - 1][j] + 1,
                                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    public List<EnglishWord> translate(String word) {
        List<EnglishWord> similarWords =  englishWordsRepository.findAll()
                .stream()
                .filter(x -> calculateLevenshtein(word, x.getWord()) <= 2)
                .collect(Collectors.toList());
        //List<Translation> translations = new LinkedList<>();
        //similarWords.forEach(w -> translations.addAll(w.getEquivalents()));
        return similarWords;
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
