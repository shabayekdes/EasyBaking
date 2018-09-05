package com.shabayekdes.easybaking.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    List<Step> steps;
    private OnStepClickListener onStepClickListener;


    public StepsAdapter(OnStepClickListener onStepClickListener) {
        this.onStepClickListener = onStepClickListener;
    }

    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        Step step = steps.get(position);
        if (position > 0) {
            holder.stepNameTV.append("Step " + String.valueOf(position) + " : ");
        }
        holder.stepNameTV.append(step.getShortDescription());

    }

    @Override
    public int getItemCount() {
        if (steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public interface OnStepClickListener {
        void onStepClicked(int position);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.stepName_textView)
        TextView stepNameTV;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            onStepClickListener.onStepClicked(position);
        }
    }
}
