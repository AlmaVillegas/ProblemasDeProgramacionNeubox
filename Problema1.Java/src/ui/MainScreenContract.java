
package ui;

public interface MainScreenContract {

    void showOutputPath(String outputPath);

    void showError(String errorMsg);

    void showFileContent(String path, String content);

    void showResults(String output);

}
