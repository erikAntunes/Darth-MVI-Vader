package com.example.androidhellomvi.View;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.androidhellomvi.Model.MainView;
import com.example.androidhellomvi.R;
import com.example.androidhellomvi.Utils.DataSource;
import com.hannesdorfmann.mosby3.mvi.MviActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;

public class MainActivity extends MviActivity<MainView,MainPresenter> implements MainView  {

    ImageView imageView;
    Button button;
    ProgressBar progressBar;

    List<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button= (Button) findViewById(R.id.btn_get_data);
        imageView = (ImageView) findViewById(R.id.recycler_data);
        imageList =  createListImage();
        progressBar = findViewById(R.id.progress_bar);


    }

    private List<String> createListImage() {

        // Lista com os Links das Imagens
        return Arrays.asList(

                "https://images-na.ssl-images-amazon.com/images/I/61flr%2BuHRpL._AC_SX425_.jpg",
                "https://pngimg.com/uploads/darth_vader/darth_vader_PNG32.png",
                "https://s2.glbimg.com/KlcHSXA4uL301PKzdK-1hVIAwOQ=/0x0:417x675/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2019/y/G/awzWEfSQmUGwixe3jVAw/darth-vader-nova.jpg",
                "https://lojatoys4fun.com.br/media/catalog/product/cache/image/700x560/e9c3970ab036de70892d86c6d221abfe/e/s/estatua-darth-vader-rogue-one-star-wars-story-07_4_1.jpg",
                "https://static3.tcdn.com.br/img/img_prod/460977/boneco_darth_vader_versao_2_0_star_wars_rah_real_action_hero_medicom_23793_1_20180925190542.png",
                "https://cdn.awsli.com.br/600x450/297/297688/produto/9254117/f2768eb2ea.jpg");
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {

        return new MainPresenter(new DataSource(imageList));
    }

    @Override
    public Observable<Integer> getImageIntent() {

        return RxView.clicks(button).map(click-> getRandomNumberInRage(0,imageList.size()-1));
    }

    private Integer getRandomNumberInRage(int min, int max)  {


        if(min >= max)
            throw new IllegalArgumentException("max must be greater than min");
        Random  r= new Random();
        return r.nextInt(max-min+1)+min;

    }

    @Override
    public void render(MainViewState viewState) {

        // Aqui processamos a mudança de Estado da View Exibida
        if(viewState.isLoading)
        {
        // Mostra a Barra de Progresso
        progressBar.setVisibility(View.VISIBLE);

        // Esconde o ImageView
        imageView.setVisibility(View.GONE);

        // Desabilita Botão
        button.setEnabled(false);

        }

        else if(viewState.error != null){

            // Esconde a Barra de Progresso
            progressBar.setVisibility(View.GONE);

            // Esconde o ImageView
            imageView.setVisibility(View.GONE);


            button.setEnabled(true);
            Toast.makeText(this,viewState.error.getMessage(),Toast.LENGTH_SHORT).show();
        }
        else if(viewState.isImageViewShow)
        {

            // Exibe ImageView
            imageView.setVisibility(View.VISIBLE);

            // Habilita Botão
            button.setEnabled(true);

            Picasso.get().load(viewState.imagelink).fetch(new Callback() {
                @Override
                public void onSuccess() {
                    imageView.setAlpha(0f);
                    Picasso.get().load(viewState.imagelink).into(imageView);
                    imageView.animate().setDuration(300).alpha(1f).start();

                    // Esconde a Barra de Progresso
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {

                    // Esconde a Barra de Progresso
                    progressBar.setVisibility(View.GONE);

                }
            });
        }
    }
}
