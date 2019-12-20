package net.yan.oschina.news.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;

import net.yan.oschina.R;
import net.yan.oschina.net.BlogResult;
import net.yan.oschina.net.URLList;
import net.yan.oschina.news.adapter.BlogAdapter;
import net.yan.oschina.news.entity.Blog;
import net.yan.oschina.util.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BlogFragment extends Fragment {
    private Unbinder binder;


    private List<Blog> lists=new ArrayList<>();
    @BindView(R.id.recyclerView_blog)
    RecyclerView recyclerView;

    BlogAdapter blogAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_blog,container,false);
        binder=ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OkHttpUtil.getDefault(this)
                .doGetAsync(HttpInfo.Builder().setUrl(URLList.GET_BLOG + ACache.get(getActivity()).getAsString("token")).build(), new Callback() {
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        BlogResult result = info.getRetDetail(BlogResult.class);
                        blogAdapter.replaceData(result.getBloglist());

                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        System.out.println(info);
                    }
                });

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        blogAdapter = new BlogAdapter(R.layout.item_blog,lists);
        recyclerView.setAdapter(blogAdapter);

        View view1 = View.inflate(getActivity(),R.layout.blog_picture,null);
        View view2 = View.inflate(getActivity(),R.layout.blog_picture2,null);
        blogAdapter.addHeaderView(view1);
        blogAdapter.addHeaderView(view2);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binder.unbind();
    }
}
