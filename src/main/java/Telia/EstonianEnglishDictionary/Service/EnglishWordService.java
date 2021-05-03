package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.EstonianWordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnglishWordService {

    @Autowired
    private EstonianWordsRepository estonianWordsRepository;

    @Autowired
    private EnglishWordsRepository englishWordsRepository;

    public List<EnglishWord> getAll() {
        return englishWordsRepository.findAll();
    }
}
