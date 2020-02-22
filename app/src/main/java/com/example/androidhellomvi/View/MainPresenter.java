package com.example.androidhellomvi.View;
import com.example.androidhellomvi.Model.MainView;
import com.example.androidhellomvi.Model.PartialMainState;
import com.example.androidhellomvi.Utils.DataSource;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter extends MviBasePresenter<MainView,MainViewState> {
    DataSource dataSource;

    public MainPresenter(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    protected void bindIntents() {

        Observable<PartialMainState> gotData = intent (MainView::getImageIntent)
                .switchMap(index -> dataSource.getImageLinkFromList(index)
                        .map(imageLink -> (PartialMainState) new PartialMainState.GotImageLink(imageLink))
                        .startWith(new PartialMainState.Loading())
                        .onErrorReturn(error -> new PartialMainState.Error(error))
                        .subscribeOn(Schedulers.io()));

        MainViewState initState = new MainViewState(false,
                false,
                "",
                null);

        Observable<PartialMainState> initIntent = gotData.observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(initIntent.scan(initState,this::viewStateReducer),MainView::render);
    }

    MainViewState viewStateReducer(MainViewState prevState,PartialMainState changeState){

        MainViewState newState = prevState;
        if (changeState instanceof PartialMainState.Loading)
        {

            newState.isLoading = true;
            newState.isImageViewShow = false;
        }
        if (changeState instanceof PartialMainState.GotImageLink)
        {

            newState.isLoading = false;
            newState.isImageViewShow = true;
            newState.imagelink = ((PartialMainState.GotImageLink)changeState).imageLink;
        }


        if (changeState instanceof PartialMainState.Error)
        {

            newState.isLoading = false;
            newState.isImageViewShow = false;
            newState.error = ((PartialMainState.Error)changeState).error;
        }

        return newState;
    }
}
