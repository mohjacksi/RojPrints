package com.mjacksi.rojprints.MainFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mjacksi.rojprints.R;
import com.mjacksi.rojprints.RealmObjects.ImageRealm;
import com.mjacksi.rojprints.RealmObjects.Project;
import com.mjacksi.rojprints.SimpleAlbum.SimpleAlbumImagesListActivity;
import com.mjacksi.rojprints.Utilises.Utilises;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectsFragment extends Fragment {

    ProjectAdapter adapter;
    public ProjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Project> projects = realm.where(Project.class).findAll();

        final ListView list = view.findViewById(R.id.project_listview);
        adapter = new ProjectAdapter(getContext(),projects);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SimpleAlbumImagesListActivity.class);
                intent.putExtra("edit","edit");
                intent.putExtra("album_id",projects.get(position).getId());
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }

    public class ProjectAdapter extends ArrayAdapter<Project> {
        public ProjectAdapter(Context context, RealmResults<Project> projects) {
            super(context, 0, projects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Project project = getItem(position);
            ImageRealm projectCover = project.getImages().first();

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_project, parent, false);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.project_image);
            Glide.with(getContext())
                    .load(projectCover.getPath())
                    .apply(new RequestOptions().placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder))
                    .into(imageView);
            TextView size = convertView.findViewById(R.id.project_size);
            TextView date = convertView.findViewById(R.id.project_date);
            size.setText(project.getSize());
            date.setText(Utilises.getTime(project.getDate()));

            // Return the completed view to render on screen
            return convertView;
        }
        public void swapItems(List<Project> items) {

            notifyDataSetChanged();
        }
    }
}
