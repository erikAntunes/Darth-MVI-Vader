package com.example.androidhellomvi.Model;

import com.example.androidhellomvi.View.MainViewState;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import io.reactivex.Observable;

public interface MainView extends MvpView {

    Observable<Integer> getImageIntent();

    void render (MainViewState viewState);
}
