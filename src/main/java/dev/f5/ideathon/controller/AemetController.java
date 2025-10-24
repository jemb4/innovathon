package dev.f5.ideathon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.f5.ideathon.model.TempDay;
import dev.f5.ideathon.services.AemetService;

@RestController
@RequestMapping("/api/aemet")
public class AemetController {

  private final AemetService aemetService;

  public AemetController(AemetService aemetService) {
    this.aemetService = aemetService;
  }

  @GetMapping("/temperaturas/{codigoMunicipio}")
  public List<TempDay> getTemperaturas(@PathVariable String codigoMunicipio) {
    return aemetService.obtenerPrediccion(codigoMunicipio);
  }

  @GetMapping("/hdd/{codigoMunicipio}")
  public Map<String, Object> getHDD(@PathVariable String codigoMunicipio,
      @RequestParam(defaultValue = "18") double tBase) {
    List<TempDay> dias = aemetService.obtenerPrediccion(codigoMunicipio);
    double hdd = aemetService.calculateHDD(dias, tBase);
    return Map.of(
        "municipio", codigoMunicipio,
        "tBase", tBase,
        "hddTotal", hdd,
        "dias", dias);
  }
}
