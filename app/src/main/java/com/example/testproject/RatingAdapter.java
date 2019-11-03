package com.example.testproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.testproject.Data.CriteriaExtended;
import com.example.testproject.Data.Grade;
import com.example.testproject.Data.GradingForm;

import java.util.ArrayList;

public class RatingAdapter extends ArrayAdapter<Grade>{

    Context context;
    private ArrayList<CriteriaExtended> questions;
    private GradingForm viewModel;

    private static class ViewHolder {
        TextView txtQuestion;
        TextView txtDetails;
        RatingBar noStars;
    }

    public RatingAdapter(Context context, ArrayList<CriteriaExtended> questions, GradingForm viewModel) {

        super(context, R.layout.grading_form_element, viewModel.getGrades());
        this.questions = questions;
        this.context = context;
        this.viewModel = viewModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Grade dataModel = getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.grading_form_element, parent, false);

            viewHolder.txtQuestion = convertView.findViewById(R.id.qname);
            viewHolder.noStars = convertView.findViewById(R.id.rating);
            viewHolder.txtDetails = convertView.findViewById(R.id.qdetails);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.noStars.setRating(dataModel.getNoStars());
        viewHolder.noStars.setId(position);
        viewHolder.noStars.setTag(dataModel.getCriteriaId());

        CriteriaExtended question = CriteriaExtended.findCriteriaById(questions, dataModel.getCriteriaId());

        if (question != null) {
            viewHolder.txtQuestion.setText(question.getName());
            viewHolder.txtDetails.setText(question.getDetails());
        }

        viewHolder.noStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    viewModel.setGrade(Integer.parseInt(ratingBar.getTag().toString()), Math.round(rating));
                }
            }
        });

        return convertView;
    }
}