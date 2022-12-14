package com.example.inventoryincidencias.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inventoryincidencias.R;
import com.example.inventoryincidencias.data.model.Dependency;
import com.example.inventoryincidencias.databinding.FragmentCreateDependencyBinding;
import com.example.inventoryincidencias.ui.base.BaseFragment;
import com.example.inventoryincidencias.ui.dependency.DependencyListViewModel;
import com.example.inventoryincidencias.ui.dependency.DependencyManagerViewModel;

import java.util.Random;

public class CreateDependencyFragment extends BaseFragment {

    FragmentCreateDependencyBinding binding;
    DependencyManagerViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateDependencyBinding.inflate(inflater, container, false);
        binding.setDependency(new Dependency());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null) {
            binding.tvTitulo.setText("Crear dependencia");
            binding.btnCreateDependency.setOnClickListener(view1 -> {
                //viewModel.add(binding.getDependency());
                viewModel.add(new Dependency(binding.tilCreateDependencyName.getEditText().getText().toString(), binding.tilCreateDependencyShortName.getEditText().getText().toString(), binding.tilCreateDependencyDescription.getEditText().getText().toString(), binding.tilCreateDependencyImage.getEditText().getText().toString()));
            });
        } else {
            binding.tvTitulo.setText("Editar dependencia");
            binding.btnCreateDependency.setText("EDITAR DEPENDENCIA");
            binding.tilCreateDependencyShortName.getEditText().setEnabled(false);
            binding.setDependency(getArguments().getParcelable(Dependency.TAG));
            binding.btnCreateDependency.setOnClickListener(view1 -> {
                //viewModel.edit(binding.getDependency());
                viewModel.edit(new Dependency(binding.tilCreateDependencyName.getEditText().getText().toString(), binding.tilCreateDependencyShortName.getEditText().getText().toString(), binding.tilCreateDependencyDescription.getEditText().getText().toString(), binding.tilCreateDependencyImage.getEditText().getText().toString()));
            });
        }

        initViewModel();
    }

    /**
     * M??todo que inicializa el ViewModel
     */
    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(DependencyManagerViewModel.class);
        viewModel.getResult().observe(getViewLifecycleOwner(), dependencyManagerResult -> {
            switch (dependencyManagerResult) {
                case NAMEEMPTY:
                    showError(R.string.errorNameEmpty);
                    break;
                case SHORTNAMEEMPTY:
                    showError(R.string.errorShortNameEmpty);
                    break;
                case SHORTNAMEFORMAT:
                    showError(R.string.errorShortNameFormat);
                    break;
                case FAILURE:
                    showError(R.string.errorDuplicate);
                    break;
                case SUCCESS:
                    showAddNotification();
                    NavHostFragment.findNavController(this).navigateUp();
                    break;
            }
        });
    }

    /**
     * M??todo que muestra una notifiaci??n con la informaci??n de la dependencia que se ha a??adido
     */
    private void showAddNotification() {
        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // 1. Crear un Bundle con la dependencia a??adida
            Bundle bundle = new Bundle();
            bundle.putParcelable(Dependency.TAG, new Dependency(binding.tilCreateDependencyName.getEditText().getText().toString(), binding.tilCreateDependencyShortName.getEditText().getText().toString(), binding.tilCreateDependencyDescription.getEditText().getText().toString(), binding.tilCreateDependencyImage.getEditText().getText().toString()));

            // 2. Si se trabaja con Activity y FragmentManager
                // Intent intent = new Intent(getActivity(), SplashActivity.class);
                // intent.putExtras(bundle);

            // 3. Se crear??a el objeto PendingIntent con el intent expl??cito
                // PendingIntent pendingIntent = PendingIntent.getActivities(getContext(), new Random().nextInt(), new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

            // 4. Si se utiliza el componente Navigation, se indicar?? el grafo de navegaci??n en el PendingIntent
            // ???? ATENCI??N !! El TAG que se utilice en el Bundle debe coincidir con el nombre del argumento que se haya indicado
            // en el grafo de navegaci??n
            PendingIntent pendingIntent = new NavDeepLinkBuilder(getContext())
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.createDependencyFragment)
                    .setArguments(bundle)
                    .createPendingIntent();


            // 5. Es crear la notificaci??n (t??tulo, contenido texto, icono...)
            builder = new Notification.Builder(getContext(), IniApplication.CHANNEL_ID);

            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(getString(R.string.app_name));
            builder.setContentText(getString(R.string.notify_add_dependency));
            builder.setAutoCancel(true);                                            // cuando pulso sobre la notificaci??n se elimina
            builder.setContentIntent(pendingIntent);

            // 6. A??adir esa notificaci??n al Manager o gestor
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(new Random().nextInt(), builder.build());
        }
    }
}