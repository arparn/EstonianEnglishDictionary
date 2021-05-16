package Telia.EstonianEnglishDictionary;

import Telia.EstonianEnglishDictionary.Model.EnglishWord;
import Telia.EstonianEnglishDictionary.Model.EstonianWord;
import Telia.EstonianEnglishDictionary.Model.Translation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@AutoConfigureMockMvc
@SpringBootTest
class EstonianEnglishDictionaryApplicationTests {

	@Autowired
	MockMvc mockTest;

	@Test
	@Order(1)
	void testAddWordAndGetAllEnglish() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/english-estonian?add=false&translation=auto&word=car").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.get("/english-estonian").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":5,\"word\":\"car\",\"equivalents\":[{\"id\":4,\"word\":\"auto\"}]}]"));
	}

	@Test
	@Order(2)
	void testAddWordAndGetAllEstonian() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/estonian-english?add=false&translation=hello&word=tere").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.get("/estonian-english").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":2,\"word\":\"taevas\",\"equivalents\":[{\"id\":1,\"word\":\"sky\"}]},{\"id\":7,\"word\":\"tere\",\"equivalents\":[{\"id\":6,\"word\":\"hello\"}]}]"));
	}

	@Test
	@Order(3)
	void testAddEmptyStringsEnglish() throws Exception {
		mockTest.perform(MockMvcRequestBuilders.post("/english-estonian?add=false&translation=&word=").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("false"));
	}

	@Test
	@Order(4)
	void testAddEmptyStringsEstonian() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/estonian-english?add=false&translation=&word=").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("false"));
	}

	@Test
	@Order(5)
	void testAddWordToBothDictionaries() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/english-estonian?add=true&translation=maailm&word=world").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.get("/english-estonian").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":5,\"word\":\"car\",\"equivalents\":[{\"id\":4,\"word\":\"auto\"}]},{\"id\":11,\"word\":\"world\",\"equivalents\":[{\"id\":10,\"word\":\"maailm\"}]}]"));
		mockTest.perform(MockMvcRequestBuilders.get("/estonian-english").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":2,\"word\":\"taevas\",\"equivalents\":[{\"id\":1,\"word\":\"sky\"}]},{\"id\":7,\"word\":\"tere\",\"equivalents\":[{\"id\":6,\"word\":\"hello\"}]},{\"id\":9,\"word\":\"maailm\",\"equivalents\":[{\"id\":8,\"word\":\"world\"}]}]"));
	}

	@Test
	@Order(6)
	void testGetTranslationEnglish() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.get("/english-estonian/translate?word=car").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("{\"wordList\":[{\"id\":5,\"word\":\"car\",\"equivalents\":[{\"id\":4,\"word\":\"auto\"}]}],\"foundExact\":true}"));
	}

	@Test
	@Order(7)
	void testGetTranslationEstonian() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.get("/estonian-english/translate?word=maailm").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("{\"wordList\":[{\"id\":9,\"word\":\"maailm\",\"equivalents\":[{\"id\":8,\"word\":\"world\"}]}],\"foundExact\":true}"));
	}

	@Test
	@Order(8)
	void testTranslationLevenshteinAlgorithm() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.get("/estonian-english/translate?word=mailm").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("{\"wordList\":[{\"id\":9,\"word\":\"maailm\",\"equivalents\":[{\"id\":8,\"word\":\"world\"}]}],\"foundExact\":false}"));
	}

	@Test
	@Order(9)
	void testDeleteWordEnglish() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.delete("/english-estonian/2").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":5,\"word\":\"car\",\"equivalents\":[{\"id\":4,\"word\":\"auto\"}]},{\"id\":11,\"word\":\"world\",\"equivalents\":[{\"id\":10,\"word\":\"maailm\"}]},{\"id\":13,\"word\":\"sheep\",\"equivalents\":[{\"id\":12,\"word\":\"lammas\"},{\"id\":14,\"word\":\"laev\"}]}]"));
	}

	@Test
	@Order(10)
	void testDeleteWordEstonian() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.delete("/estonian-english/4").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":2,\"word\":\"taevas\",\"equivalents\":[{\"id\":1,\"word\":\"sky\"}]}]"));
	}

	@Test
	@Order(11)
	void testAddMultipleTranslationsToOneWordAndDeleteOneTranslationEnglish() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/english-estonian?add=false&translation=lammas&word=sheep").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.post("/english-estonian?add=false&translation=laev&word=sheep").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.delete("/english-estonian/10/11").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":5,\"word\":\"car\",\"equivalents\":[{\"id\":4,\"word\":\"auto\"}]},{\"id\":11,\"word\":\"world\",\"equivalents\":[{\"id\":10,\"word\":\"maailm\"}]},{\"id\":13,\"word\":\"sheep\",\"equivalents\":[{\"id\":12,\"word\":\"lammas\"},{\"id\":14,\"word\":\"laev\"}]}]"));
	}

	@Test
	@Order(12)
	void testAddMultipleTranslationsToOneWordAndDeleteOneTranslationEstonian() throws Exception {

		mockTest.perform(MockMvcRequestBuilders.post("/estonian-english?add=false&translation=sky&word=taevas").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.post("/estonian-english?add=false&translation=ski&word=taevas").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("true"));
		mockTest.perform(MockMvcRequestBuilders.delete("/estonian-english/2/3").contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON)).andExpect(content().string("[{\"id\":2,\"word\":\"taevas\",\"equivalents\":[{\"id\":1,\"word\":\"sky\"}]}]"));
	}
}
