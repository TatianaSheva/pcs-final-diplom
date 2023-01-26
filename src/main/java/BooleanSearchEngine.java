import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Реализация поискового движка.
 * Слово Boolean пришло из теории информационного поиска, тк наш движок будет искать в тексте ровно то слово,
 * которое было указано, без использования синонимов и прочих приёмов нечёткого поиска
 */

public class BooleanSearchEngine implements SearchEngine {
    private final Map<String, List<PageEntry>> map = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        for (File pdf : pdfsDir.listFiles()) {
            var document = new PdfDocument(new PdfReader(pdf));
            int pageNumber = document.getNumberOfPages();
            for (int i = 1; i <= pageNumber; i++) {
                var page = document.getPage(i);
                var text = PdfTextExtractor.getTextFromPage(page);
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }

                for (String word : freqs.keySet()) {
                    PageEntry pageEntry = new PageEntry(pdf.getName(), i, freqs.get(word));
                    if (map.containsKey(word)) {
                        map.get(word).add(pageEntry);

                    } else {
                        map.put(word, new ArrayList<>());
                        map.get(word).add(pageEntry);
                    }
                    //Сортировка
                    map.values().forEach(Collections::sort);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        //Создаем пустой список с результатами поиска
        List<PageEntry> result = new ArrayList<>();
        //Переводим слова в нижний регистра
        String wordToLowerCase = word.toLowerCase();
        //Если слово есть в базе - перебираем результаты поиска и добавляем их в список
        if (map.get(wordToLowerCase) != null) {
            for (PageEntry pageEntry : map.get(wordToLowerCase)) {
                result.add(pageEntry);
            }
        }
       // Collections.sort(result);
        return result;
    }
}
