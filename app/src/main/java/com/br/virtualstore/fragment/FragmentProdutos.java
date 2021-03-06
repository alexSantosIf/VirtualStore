package com.br.virtualstore.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.br.virtualstore.R;
import com.br.virtualstore.adapter.ProdutoRecyclerAdapter;
import com.br.virtualstore.entity.Produto;
import com.br.virtualstore.util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Alex on 02/01/2018.
 */

public class FragmentProdutos extends Fragment {

    private RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_produtos, container, false);

        final RelativeLayout lytLoading = viewRoot.findViewById(R.id.lytLoading);
        lytLoading.setVisibility(View.VISIBLE);

        rv = viewRoot.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        new AsyncHttpClient().get(Constants.URL_WS_BASE + "produto/list", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (response != null) {
                    Type type = new TypeToken<List<Produto>>() {
                    }.getType();
                    List<Produto> produtos = new Gson().fromJson(response.toString(), type);

                    ProdutoRecyclerAdapter adapter = new ProdutoRecyclerAdapter(produtos);
                    rv.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Houve um erro no carregamento da lista de profissões...", Toast.LENGTH_SHORT).show();
                }

                lytLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), "Falha: " + responseString, Toast.LENGTH_SHORT).show();
            }
        });

        return viewRoot;
    }
}
