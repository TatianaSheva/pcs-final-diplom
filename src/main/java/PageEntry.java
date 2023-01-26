import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс, описывающий один элемент результата одного поиска.
 * Он состоит из имени пдф-файла, номера страницы и количества раз, которое встретилось это слово на ней.
 */

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return o.count - this.count;
    }

    @Override
    public String toString() {
        Map resultMap = new LinkedHashMap();
        resultMap.put("pdfName", pdfName);
        resultMap.put("page", page);
        resultMap.put("count", count);
        JSONObject result = new JSONObject(resultMap);
        return result.toString();
    }
}


