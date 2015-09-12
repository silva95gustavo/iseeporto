package antonio.iseeporto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Antonio on 08-09-2015.
 */
public class Place extends android.app.Fragment {

    Double latitude, longitude;
    Boolean report = false;
    String id;
    DownloaderImage downloadImage = new DownloaderImage();
    TextView addressText, reportText;
    JSONObject objInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTemp = inflater.inflate(R.layout.place, container, false);

        JSONAsyncTask temp = new JSONAsyncTask() {
            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (!result) {
                    return;
                }
                objInfo = jsono;
                shortcut(result);
            }
        };
        temp.setActivity(getActivity());

        report = false;
        temp.execute("https://iseeporto.revtut.net/api/api.php?action=get_poi_info&id=" + SingletonStringId.getInstance().getId());
        return viewTemp;
    }

    public void createVisitFrag()
    {
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        VisitButton v = new VisitButton();
        transaction.add(R.id.avaliacao, v, "VisitButton");
        transaction.commit();
    }

    public void visitarLocal()
    {
        EvaluateThumbs avaliacao = new EvaluateThumbs();
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.avaliacao, avaliacao, "Avaliar");
        transaction.commit();
    }

    public void retirarVisita()
    {
        VisitButton v = new VisitButton();
        android.app.FragmentManager manager = getFragmentManager();
        android.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.avaliacao, v, "VisitButton");
        transaction.commit();
    }

    private void shortcut(Boolean result)
    {
        getView().post(new Runnable() {
            @Override
            public void run() {

                if (!report) {
                    try {
                        if (objInfo != null) {
                            ((TextView) getView().findViewById(R.id.namePlaceId)).setText(objInfo.getString("name"));
                            addressText = (TextView) getView().findViewById(R.id.idAddress);
                            addressText.setText(objInfo.getString("address"));
                            reportText = (TextView) getView().findViewById(R.id.reportId);
                            ((TextView) getView().findViewById(R.id.idDescription)).setText(objInfo.getString("description"));
                            latitude = Double.parseDouble(objInfo.getString("latitude"));
                            longitude = Double.parseDouble(objInfo.getString("longitude"));
                            ((TextView) getView().findViewById(R.id.placeInfoId)).setText(
                                    objInfo.getString("numVisits") + " visits, " +
                                            objInfo.getString("numLikes") + " likes and " +
                                            objInfo.getString("numDislikes") + " dislikes");
                            id = objInfo.getString("id");
                            downloadImage.downloadImage((ImageView) getView().findViewById(R.id.placePicId), getView(), "https://iseeporto.revtut.net/uploads/PoI_photos/" + id + ".jpg");
                            setClicks();
                            createVisitFrag();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void openNavigation(double latitude, double longitude) {
        //o ponto de partida é o local onde o utilizador está
        Intent intent1 = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
        startActivity(intent1);
    }

    private void setClicks()
    {
        reportText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report = true;
                JSONAsyncTask tempTask = new JSONAsyncTask();
                tempTask.setActivity(getActivity());
                tempTask.execute("https://iseeporto.revtut.net/api/api.php?action=report&id=" +
                        id + "&accesstoken=" + Singleton.getInstance().getAccessToken().getToken());
                Toast.makeText(getActivity().getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT).show();
            }
        });

        addressText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abre o mapa
                openNavigation(latitude, longitude);
            }
        });
    }
}
