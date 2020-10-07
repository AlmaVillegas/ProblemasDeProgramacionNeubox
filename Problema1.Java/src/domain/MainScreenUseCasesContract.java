package domain;

public interface MainScreenUseCasesContract {

    void loadOutputPath(Callbacks callbacks);

    void changeOutputPath(String newPath, Callbacks callbacks);

    void readSelectedFile(String path, Callbacks callbacks);

    interface Callbacks {

        void onLoadOutputPathSuccess(String path);

        void onLoadOutputPathFailure(String errorMsg);


        void onChangeOutputPathSuccess(String newPath);

        void onChangeOutputPathFailure(String newPath);


        void onReadSelectedFileSuccess(String path, String content, String output);

        void onReadSelectedFileFailure(String error);

    }

}
