package Telia.EstonianEnglishDictionary.Repository;

import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstonianWordsRepository extends JpaRepository<EstonianWord, Long> { }
