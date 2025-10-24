package dev.f5.ideathon.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dev.f5.ideathon.model.TempDay;

@Service
public class AemetService {

  @Value("${aemet.api.key}")
  private String apiKey;

  private static final String BASE_URL = "https://opendata.aemet.es/opendata/api/prediccion/especifica/municipio/diaria/";

  public List<TempDay> obtenerPrediccion(String codigoMunicipio) {
    RestTemplate restTemplate = new RestTemplate();

    String url = BASE_URL + codigoMunicipio + "?api_key=" + apiKey;
    JSONObject json = new JSONObject(restTemplate.getForObject(url, String.class));

    String urlDatos = json.getString("datos");

    JSONArray prediccionArray = new JSONArray(restTemplate.getForObject(urlDatos, String.class));
    JSONObject prediccion = prediccionArray.getJSONObject(0);

    JSONArray dias = prediccion.getJSONObject("prediccion").getJSONArray("dia");

    List<TempDay> resultado = new ArrayList<>();
    for (int i = 0; i < Math.min(7, dias.length()); i++) {
      JSONObject dia = dias.getJSONObject(i);
      String fecha = dia.getString("fecha");
      int tMin = dia.getJSONObject("temperatura").getInt("minima");
      int tMax = dia.getJSONObject("temperatura").getInt("maxima");
      resultado.add(new TempDay(fecha, tMin, tMax));
    }

    return resultado;
  }

  public double calculateHDD(List<TempDay> dias, double tBase) {
    double hdd = 0.0;
    double T_BASE = 18L;

    for (TempDay dia : dias) {
      double tMax = dia.gettMax();
      double tMin = dia.gettMin();
      double tAvg = (dia.gettMax() + dia.gettMin()) / 2.0;
      if (tMax < T_BASE) {
        hdd += T_BASE - tAvg;
      } else if (tAvg < T_BASE && T_BASE < tMax) {
        hdd += ((T_BASE - tMin) / 2.0) - ((tMax - T_BASE) / 4.0);
      } else if (tMin < T_BASE && T_BASE < tAvg) {
        hdd += (T_BASE - tMin) / 4.0;
      } else {
        hdd = 0.0;
      }
    }

    return hdd;
  }

}
