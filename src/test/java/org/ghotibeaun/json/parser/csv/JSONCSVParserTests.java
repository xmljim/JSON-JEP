package org.ghotibeaun.json.parser.csv;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.parser.ParserFactory;
import org.junit.Ignore;
import org.junit.Test;

public class JSONCSVParserTests {

    @Test //@Ignore
    public void testParserNoHeaderJSONSettings() {
        try (InputStream inputStream = getClass().getResourceAsStream("/csvsettings-covid-noheader.json");
                InputStream csvData = getClass().getResourceAsStream("/covid-noheader-sample.csv");) {
            final CSVSettings settings = CSVSettings.fromConfiguration(inputStream);
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);

            final JSONNode results = parser.parse(csvData);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

    @Test //@Ignore
    public void testParserHeaderJSONSettings() {
        try (InputStream inputStream = getClass().getResourceAsStream("/csvsettings-covid-header.json");
                InputStream csvData = getClass().getResourceAsStream("/covid-header-sample2.csv");) {
            final CSVSettings settings = CSVSettings.fromConfiguration(inputStream);
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);

            final JSONNode results = parser.parse(csvData);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }        
    }

    @Test //@Ignore
    public void testParseHeaderNoSettings() {
        try (InputStream csvData = getClass().getResourceAsStream("/covid-header-sample2.csv");) {
            final CSVSettings settings = CSVSettings.getDefaultSettings();
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);

            final JSONNode results = parser.parse(csvData);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }   
    }

    @Test //@Ignore
    public void testIrregularData() {
        try (InputStream inputStream = getClass().getResourceAsStream("/csvsettings-covid-header.json");
                InputStream csvData = getClass().getResourceAsStream("/covid-header-funkydata.csv");) {
            final CSVSettings settings = CSVSettings.fromConfiguration(inputStream);
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);

            final JSONNode results = parser.parse(csvData);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }       
    }

    @Test //@Ignore
    public void testIrregularData2() {
        try (InputStream inputStream = getClass().getResourceAsStream("/csvsettings-covid-header.json");
                InputStream csvData = getClass().getResourceAsStream("/covid-header-weirddata.csv");) {
            final CSVSettings settings = CSVSettings.fromConfiguration(inputStream);
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);

            final JSONNode results = parser.parse(csvData);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }       
    }

    @Test @Ignore
    public void testFromUrl() {
        final String url = "https://raw.githubusercontent.com/nytimes/covid-19-data/master/us-counties.csv";
        try {
            final URL pullUrl = new URL(url);
            try (InputStream settingsStream = getClass().getResourceAsStream("/csvsettings-covid-header.json")) {
                final CSVSettings settings = CSVSettings.fromConfiguration(settingsStream);
                final JSONCSVParser parser = ParserFactory.getCsvParser(settings);
                final JSONNode results = parser.parse(pullUrl);
                System.out.println(results.asJSONArray().size());
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fail();
            }
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

    @Test //@Ignore
    public void testJHConfirmedSample() {

        try (InputStream dataStream = getClass().getResourceAsStream("/covid-johns-hopkins-confirmed-sample.csv")) {
            final CSVSettings settings = CSVSettings.getDefaultSettings();
            final JSONCSVParser parser = ParserFactory.getCsvParser(settings);
            final JSONNode results = parser.parse(dataStream);
            System.out.println(results.asJSONArray().size());
        } catch (final IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test @Ignore
    public void testJHConfirmedUrl() {
        final String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_US.csv";
        try {
            final URL pullUrl = new URL(url);
            try (InputStream settingsStream = getClass().getResourceAsStream("/standard-csv-header.json")) {
                final CSVSettings settings = CSVSettings.fromConfiguration(settingsStream);
                final JSONCSVParser parser = ParserFactory.getCsvParser(settings);
                final JSONNode results = parser.parse(pullUrl);
                System.out.println(results.asJSONArray().size());
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fail();
            }
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail();
        }
    }

}
