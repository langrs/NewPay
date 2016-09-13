package com.unioncloud.newpay.presentation.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.esummer.android.FragmentStackDelegate;
import com.esummer.android.fragment.StatedFragment;
import com.esummer.android.stateupdatehandler.StateUpdateHandlerListener;
import com.esummer.android.updatehandler.UpdateCompleteCallback;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.presentation.model.notice.NoticeModel;
import com.unioncloud.newpay.presentation.presenter.notice.QueryNoticeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwj on 16/9/12.
 */
public class NoticesFragment extends StatedFragment {

    public static NoticesFragment newInstance() {
        NoticesFragment fragment = new NoticesFragment();
        return fragment;
    }

    private static StateUpdateHandlerListener<NoticesFragment, QueryNoticeHandler> queryHandlerListener
            = new StateUpdateHandlerListener<NoticesFragment, QueryNoticeHandler>() {
        @Override
        public void onUpdate(String key, NoticesFragment handler, QueryNoticeHandler response) {
            handler.dealQuery(response);
        }

        @Override
        public void onCleanup(String key, NoticesFragment handler, QueryNoticeHandler response) {
            response.removeCompletionListener(handler.queryListener);
        }
    };

    private UpdateCompleteCallback<QueryNoticeHandler> queryListener =
            new UpdateCompleteCallback<QueryNoticeHandler>() {
        @Override
        public void onCompleted(QueryNoticeHandler response, boolean isSuccess) {
            dealQuery(response);
        }
    };

    private View titleContainer;
    private ListView listView;
    NoticesAdapter adapter;

    private FragmentStackDelegate stackDelegate;

    public void setDialogMode(boolean isDialog) {
        getArguments().putBoolean("isDialog", isDialog);
    }

    private boolean isDialog() {
        return getArguments().getBoolean("isDialog");
    }

    public void setNotices(List<Notice> list) {
        getArguments().putParcelableArrayList("notices", mapperNotice(list));
    }

    private ArrayList<NoticeModel> getNotices() {
        return getArguments().getParcelableArrayList("notices");
    }

    private static ArrayList<NoticeModel> mapperNotice(List<Notice> list) {
        if (list == null) {
            return null;
        }
        ArrayList<NoticeModel> models = new ArrayList<>(list.size());
        for (Notice notice : list) {
            NoticeModel model = new NoticeModel();
            model.setTitle(notice.getTitle());
            model.setContent(notice.getContent());
            models.add(model);
        }
        return models;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notices, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleContainer = view.findViewById(R.id.fragment_notices_title_container);
        if (!isDialog()) {
            titleContainer.setVisibility(View.GONE);
        }
        listView = (ListView) view.findViewById(R.id.fragment_notices_list);
        adapter = new NoticesAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeModel notice = (NoticeModel) parent.getAdapter().getItem(position);
                toDetail(notice);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stackDelegate = (FragmentStackDelegate) getActivity();
        if (isDialog()) {
            showNotices(getNotices());
        } else {
            requestQuery();
        }
    }

    private void toDetail(NoticeModel notice) {
        if (isDialog()) {
            NoticeDetailFragment fragment = NoticeDetailFragment.newInstance(notice);
            fragment.show(getParentFragment().getChildFragmentManager(), "noticeDetail");
            dismiss();
        } else {
            NoticeDetailFragment fragment = NoticeDetailFragment.newInstance(notice);
            stackDelegate.push(this, fragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateForResponse("NoticesFragment:query");
    }

    private void requestQuery() {
        QueryNoticeHandler handler = new QueryNoticeHandler(null);
        updateForResponse("NoticesFragment:query", handler, queryHandlerListener);
        handler.run();
    }

    private void  dealQuery(QueryNoticeHandler handler) {
        if (handler == null) {
            return;
        }
        synchronized (handler.getStatusLock()) {
            if (handler.isUpdating()) {
                showProgressDialog("正在查询...");
                handler.addCompletionListener(queryListener);
            } else {
                if (isVisible()) {
                    dismissProgressDialog();
                    cleanupResponse("NoticesFragment:query");
                    if (handler.isSuccess()) {
                        showNotices(mapperNotice(handler.getData()));
                    } else {
                        showToast("没有查询到数据");
                    }
                }
            }
        }
    }

    private void showNotices(List<NoticeModel> list) {
        adapter.setList(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
        listView = null;
        stackDelegate = null;
    }

}
