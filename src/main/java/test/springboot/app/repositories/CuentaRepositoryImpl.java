package test.springboot.app.repositories;

import test.springboot.app.Datos;
import test.springboot.app.models.Cuenta;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class CuentaRepositoryImpl implements CuentaRepository {

    private final Map<Long, Cuenta> cuentas = new ConcurrentHashMap<>();

    public CuentaRepositoryImpl() {
        Cuenta c1 = Datos.crearCuenta001();
        Cuenta c2 = Datos.crearCuenta002();
        cuentas.put(c1.getId(), c1);
        cuentas.put(c2.getId(), c2);
    }

    @Override
    public List<Cuenta> findAll() {
        return cuentas.values().stream().collect(Collectors.toList());
    }

    @Override
    public Cuenta findById(Long id) {
        return cuentas.get(id);
    }

    @Override
    public void update(Cuenta cuenta) {
        cuentas.put(cuenta.getId(), cuenta);
    }
}