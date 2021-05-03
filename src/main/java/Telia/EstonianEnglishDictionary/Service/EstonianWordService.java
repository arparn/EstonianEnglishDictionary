package Telia.EstonianEnglishDictionary.Service;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Repository.EnglishWordsRepository;
import Telia.EstonianEnglishDictionary.Repository.EstonianWordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstonianWordService {

    @Autowired
    private EstonianWordsRepository estonianWordsRepository;

    @Autowired
    private EnglishWordsRepository englishWordsRepository;

    public List<EstonianWord> getAll() {
        return estonianWordsRepository.findAll();
    }
}
