package presentation;

import domain.MainScreenUseCases;
import domain.MainScreenUseCasesContract;
import ui.MainScreenContract;

import javax.swing.*;

public final class MainScreenPresenter implements MainScreenPresenterContract, MainScreenUseCasesContract.Callbacks {

    private MainScreenContract view;

    private MainScreenUseCasesContract useCases;

    public MainScreenPresenter(MainScreenContract view) {
        this.view = view;
        this.useCases = new MainScreenUseCases();
    }

    @Override
    public void windowStarted() {
        useCases.loadOutputPath(this);
    }

    @Override
    public void changeOutputPath(int result, String path) {
        if (result == JFileChooser.APPROVE_OPTION && path != null) {
            useCases.changeOutputPath(path, this);
        }
    }

    @Override
    public void openFile(int result, String path) {
        if (result == JFileChooser.APPROVE_OPTION && path != null) {
            useCases.readSelectedFile(path, this);
        }
    }


    @Override
    public void onLoadOutputPathSuccess(String path) {
        view.showOutputPath(path);
    }

    @Override
    public void onLoadOutputPathFailure(String errorMsg) {
        view.showError(errorMsg);
    }


    @Override
    public void onChangeOutputPathSuccess(String newPath) {
        view.showOutputPath(newPath);
    }

    @Override
    public void onChangeOutputPathFailure(String newPath) {
        view.showError(newPath);
    }


    @Override
    public void onReadSelectedFileSuccess(String path, String content, String output) {
        view.showFileContent(path, content);
        view.showResults(output);
    }

    @Override
    public void onReadSelectedFileFailure(String error) {
        view.showError(error);
    }

}
