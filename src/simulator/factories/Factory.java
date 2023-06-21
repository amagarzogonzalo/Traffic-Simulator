package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import exceptions.NewSetContClassEventException;
import exceptions.SetWeatherEventException;

public interface Factory<T> {
	public T createInstance(JSONObject info) throws JSONException, SetWeatherEventException, NewSetContClassEventException;
}
