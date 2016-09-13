package com.unioncloud.newpay.presentation.ui.notice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esummer.android.fragment.StatedFragment;
import com.unioncloud.newpay.R;
import com.unioncloud.newpay.domain.model.notice.Notice;
import com.unioncloud.newpay.presentation.model.notice.NoticeModel;

/**
 * Created by cwj on 16/9/12.
 */
public class NoticeDetailFragment extends StatedFragment {

    public static NoticeDetailFragment newInstance(NoticeModel notice) {
        NoticeDetailFragment fragment = new NoticeDetailFragment();
        fragment.setNotice(notice);
        return fragment;
    }

    public static NoticeDetailFragment newInstance(Notice notice) {
        NoticeModel noticeModel = new NoticeModel();
        noticeModel.setTitle(notice.getTitle());
        noticeModel.setContent(notice.getContent());

        NoticeDetailFragment fragment = new NoticeDetailFragment();
        fragment.setNotice(noticeModel);
        return fragment;
    }

    private TextView titleTv;
    private TextView contentTv;
//    private TextView footerTv;

    private void setNotice(NoticeModel notice) {
        getArguments().putParcelable("notice", notice);
    }

    private NoticeModel getNotice() {
        return (NoticeModel) getArguments().getParcelable("notice");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleTv = (TextView) view.findViewById(R.id.fragment_notice_detail_title);
        contentTv = (TextView) view.findViewById(R.id.fragment_notice_detail_content);
//        footerTv = (TextView) view.findViewById(R.id.fragment_notice_detail_footer);

        NoticeModel notice = getNotice();
        titleTv.setText(notice.getTitle());
        contentTv.setText(notice.getContent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
