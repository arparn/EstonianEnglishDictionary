package Telia.EstonianEnglishDictionary.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EstonianWord {
    @Id
    @GeneratedValue
    private Long id;
    private String word;

    @ManyToMany
    private List<Translation> equivalents = new ArrayList<>();

    public EstonianWord(String word, Translation translation) {
        this.word = word;
        equivalents.add(translation);
    }
}
