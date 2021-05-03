package Telia.EstonianEnglishDictionary.Repository;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnglishWordsRepository extends JpaRepository<EnglishWord, Long> { }
