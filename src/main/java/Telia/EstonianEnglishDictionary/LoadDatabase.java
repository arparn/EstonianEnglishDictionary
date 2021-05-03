package Telia.EstonianEnglishDictionary;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.EstonianWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.TranslationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Slf4j
public class LoadDatabase {

    @Autowired
    private TranslationRepository translationRepository;

    @Bean
    CommandLineRunner initDatabase(EnglishWordsRepository englishWordsRepository, EstonianWordsRepository estonianWordsRepository) {
        return args -> {
            Translation translation1 = new Translation("tere");
            translationRepository.save(translation1);
            Translation translation2 = new Translation("hello");
            translationRepository.save(translation2);
            List<EnglishWord> englishWords = List.of(
                    new EnglishWord("hello", translation1)
            );

            List<EstonianWord> estonianWords = List.of(
                    new EstonianWord("tere", translation2)
            );

            englishWordsRepository.saveAll(englishWords);
            estonianWordsRepository.saveAll(estonianWords);
        };
    }
}
