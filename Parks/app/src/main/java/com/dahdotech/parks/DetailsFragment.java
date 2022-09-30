package com.dahdotech.parks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dahdotech.parks.adapter.ViewPagerAdapter;
import com.dahdotech.parks.databinding.FragmentParksBinding;
import com.dahdotech.parks.model.ParkViewModel;

import org.w3c.dom.Text;

public class DetailsFragment extends Fragment {
    private ParkViewModel parkViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.details_view_pager);

        TextView parkName = view.findViewById(R.id.details_park_name_text_view);
        TextView parkDesignation = view.findViewById(R.id.details_park_designation_text_view);
        TextView description = view.getRootView().findViewById(R.id.details_description);
        TextView activities = view.getRootView().findViewById(R.id.details_activities);
        TextView entranceFees = view.getRootView().findViewById(R.id.details_entrancefees);
        TextView opHours = view.getRootView().findViewById(R.id.details_operatinghours);
        TextView detailsTopics = view.getRootView().findViewById(R.id.details_topics);
        TextView directions = view.getRootView().findViewById(R.id.details_directions);

        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), park -> {
            parkName.setText(park.getName());
            parkDesignation.setText(park.getDesignation());
            description.setText(park.getDescription());

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < park.getActivities().size(); i++) {
                stringBuilder.append(park.getActivities().get(i).getName())
                        .append(" | ");
            }
            activities.setText(stringBuilder);

            if(park.getEntranceFees().size() > 0){
                entranceFees.setText(String.format("Cost: $%s", park.getEntranceFees().get(0).getCost()));
            }
            else{
                entranceFees.setText(R.string.info_unavailable);
            }

            StringBuilder opsString = new StringBuilder();
            opsString.append("Wednesday: ").append(park.getOperatingHours().get(0).getStandardHours().getWednesday()).append("\n")
                    .append("Monday: ").append(park.getOperatingHours().get(0).getStandardHours().getMonday()).append("\n")
                    .append("Thursday: ").append(park.getOperatingHours().get(0).getStandardHours().getThursday()).append("\n")
                    .append("Sunday: ").append(park.getOperatingHours().get(0).getStandardHours().getSunday()).append("\n")
                    .append("Tuesday: ").append(park.getOperatingHours().get(0).getStandardHours().getTuesday()).append("\n")
                    .append("Friday: ").append(park.getOperatingHours().get(0).getStandardHours().getFriday()).append("\n")
                    .append("Saturday: ").append(park.getOperatingHours().get(0).getStandardHours().getSaturday());

            opHours.setText(opsString);

            StringBuilder topicBuilder = new StringBuilder();
            for (int i = 0; i < park.getTopics().size() ; i++) {
                topicBuilder.append(park.getTopics().get(i).getName()).append(" | ");
            }
            detailsTopics.setText(topicBuilder);

            if (!TextUtils.isEmpty(park.getDirectionsInfo())) {
                directions.setText(park.getDirectionsInfo());
            }else {
                directions.setText(R.string.no_directions);
            }


            viewPagerAdapter = new ViewPagerAdapter(park.getImages());
            viewPager.setAdapter(viewPagerAdapter);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parkViewModel = new ViewModelProvider(requireActivity())
                .get(ParkViewModel.class);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
}