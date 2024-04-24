import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public class Spreadsheet {

    class Cell {
        private String content;
        private Integer value;
        private final Spreadsheet spreadsheet;

        public Cell(String content, Spreadsheet spreadsheet) {
            this.spreadsheet = spreadsheet;
            this.content = content;
            this.value = null;
        }

        public void setValue(String content) {
          this.content = content;
          value = null;
        }

        public Integer getValue() {
            if(nonNull(value)) {
                return value;
            }
            value = evaluate();
            return value;
        }

        private Integer evaluate() {
            List<String> tokens = extractTokens();
            Integer result = 0;
            String lastOperator = "+";
            Integer tokenValue = 0;
            for(int i = 0; i < tokens.size(); i++) {
                if(tokens.get(i).equals("+") || tokens.get(i).equals("-")) {
                    lastOperator = tokens.get(i);
                    continue;
                } else if(tokens.get(i).matches("\\d+")) {
                    tokenValue = Integer.parseInt(tokens.get(i));
                } else if(tokens.get(i).matches("^[A-Z]+\\d+$")){
                    tokenValue = spreadsheet.getCellContent(tokens.get(i));
                }
                if(lastOperator.equals("+")) {
                    result += tokenValue;
                } else {
                    result -= tokenValue;
                }
            }
            return result;
        }

        private List<String> extractTokens() {
            String trimmedExpression = content.replaceAll("\\s","");
            List<String> tokens = new ArrayList<>();
            tokens.add("0");
            for(int i = 0 ; i < trimmedExpression.length(); i++) {
                StringBuilder currentToken = new StringBuilder();
                while(i < trimmedExpression.length() && !(trimmedExpression.charAt(i) == '+' || trimmedExpression.charAt(i) == '-')) {
                    currentToken.append(trimmedExpression.charAt(i));
                    i++;
                }
                tokens.add(currentToken.toString());
                if(i < trimmedExpression.length()) {
                    tokens.add(String.valueOf(trimmedExpression.charAt(i)));
                }
            }
            return tokens;
        }
    }

    private Map<String, Cell> cells;

    public Spreadsheet() {
        cells = new HashMap<>();
    }

    public void setCellContent(String key, String content) {
        if(cells.containsKey(key)) {
            cells.get(key).setValue(content);
        } else {
            Cell cell = new Cell(content, this);
            cells.put(key, cell);
        }
    }

    public Integer getCellContent(String key) {
        if(!cells.containsKey(key)) {
            return 0;
        } else {
            return cells.get(key).getValue();
        }
    }
}
