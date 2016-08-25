package procedural;

import map.Map;
import metrics.Metric;
import metrics.MetricKey;
import model.BiomeType;
import model.Terrain;
import model.TerrainType;

public class BiomeGeneration {

    private Map mMap;

    private TemperatureGeneration mTemperatureGeneration;

    private HumidityGeneration mHumidityGeneration;

    public BiomeGeneration(Map map) {
        mMap = map;
        mTemperatureGeneration = new TemperatureGeneration(mMap);
        mHumidityGeneration = new HumidityGeneration(mMap);
    }

    public void generate() {
        Metric.start(MetricKey.TEMPERATUREGENERATION);
        mTemperatureGeneration.generate();
        Metric.record(MetricKey.TEMPERATUREGENERATION);
        Metric.start(MetricKey.HUMIDITYGENERATION);
        mHumidityGeneration.generate();
        Metric.record(MetricKey.HUMIDITYGENERATION);

        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x < mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);

                if (terrain.getTerrainType().equals(TerrainType.WATER) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER) ||
                        terrain.getTerrainType().equals(TerrainType.BEACH) ||
                        terrain.getTerrainType().equals(TerrainType.RIVER_BANK) ||
                        terrain.getTerrainType().equals(TerrainType.MOUNTAIN)) {
                    continue;
                }

                terrain.setBiomeType(BiomeType.getBiomeTypeByTempAndHumidity(terrain.getTemperature(),
                        terrain.getHumidity()));
            }
        }
    }
}
