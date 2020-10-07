package presentation;

public interface MainScreenPresenterContract {

    void windowStarted();

    void changeOutputPath(int result, String path);

    void openFile(int result, String path);

}
