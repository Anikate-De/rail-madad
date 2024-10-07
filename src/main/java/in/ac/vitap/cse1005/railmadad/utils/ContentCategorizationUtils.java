package in.ac.vitap.cse1005.railmadad.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.ac.vitap.cse1005.railmadad.domain.enums.ComplaintCategory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.experimental.UtilityClass;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

/** Utility class for categorizing content based on annotations, words, and sentiment. */
@UtilityClass
public class ContentCategorizationUtils {

  private static final String KEYWORDS_FILE = "data/complaint_keywords.json";

  /**
   * Loads keywords for categorization from a JSON file.
   *
   * @return a map of ComplaintCategory to a list of keywords
   * @throws IOException if an I/O error occurs during file reading
   */
  public static Map<ComplaintCategory, List<String>> loadKeywords() throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    // Load the keywords file from the classpath
    InputStream inputStream =
        ContentCategorizationUtils.class.getClassLoader().getResourceAsStream(KEYWORDS_FILE);
    if (inputStream == null) {
      throw new IllegalArgumentException("File not found: " + KEYWORDS_FILE);
    }

    @SuppressWarnings("unchecked")
    Map<String, List<String>> jsonMap = mapper.readValue(inputStream, Map.class);

    // Convert the JSON map to a map with ComplaintCategory keys
    Map<ComplaintCategory, List<String>> keywordMap = new HashMap<>();
    for (Map.Entry<String, List<String>> entry : jsonMap.entrySet()) {
      ComplaintCategory category = ComplaintCategory.fromCategoryName(entry.getKey());
      keywordMap.put(category, entry.getValue());
    }

    return keywordMap;
  }

  /**
   * Returns a category based on the annotations, words, and sentiment of the provided content.
   *
   * @param content the content to be analyzed for categorization
   * @param index the Lucene index containing the keywords
   * @param analyzer the Lucene analyzer to be used for parsing the content
   * @return the corresponding ComplaintCategory based on the content analysis
   * @throws IOException if an I/O error occurs during index reading
   * @throws ParseException if an error occurs during query parsing
   */
  public static ComplaintCategory getCategory(String content, Directory index, Analyzer analyzer)
      throws IOException, ParseException {
    // Open the index for reading
    DirectoryReader reader = DirectoryReader.open(index);
    IndexSearcher searcher = new IndexSearcher(reader);

    // Parse the content to create a query
    QueryParser parser = new QueryParser("keyword", analyzer);
    Query query = parser.parse(content);

    // Search the index for matching documents
    TopDocs results = searcher.search(query, 10);
    ScoreDoc[] hits = results.scoreDocs;

    ComplaintCategory bestCategory = ComplaintCategory.MISCELLANEOUS;
    float highestScore = 0;

    // Iterate through the search results to find the best matching category
    for (ScoreDoc hit : hits) {
      Document doc = searcher.storedFields().document(hit.doc);
      ComplaintCategory category = ComplaintCategory.fromCategoryName(doc.get("category"));
      float score = hit.score;
      if (score > highestScore) {
        highestScore = score;
        bestCategory = category;
      }
    }

    // Close the index reader
    reader.close();
    return bestCategory;
  }
}
