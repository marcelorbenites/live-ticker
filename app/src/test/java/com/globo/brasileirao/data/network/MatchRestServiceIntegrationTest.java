package com.globo.brasileirao.data.network;


import com.globo.brasileirao.entities.LiveTickerEntry;
import com.globo.brasileirao.entities.Match;

import org.junit.Test;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.observers.TestSubscriber;

import static junit.framework.TestCase.assertEquals;


public class MatchRestServiceIntegrationTest {

    @Test public void getMatchesSuccess() throws Exception {

        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                        .setResponseCode(200)
                        .addHeader("Content-Type", "application/json; charset=utf-8")
                        .setBody("[  \n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b53b44e4b0762325b1b64b\"\n" +
                                "      },\n" +
                                "      \"matchId\":1,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Grêmio\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/gremio_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Fluminense\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/05/05/fluminense-escudo-65x65.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":3,\n" +
                                "      \"awayScore\":1,\n" +
                                "      \"date\":\"2015-01-22T19:36:14.433Z\",\n" +
                                "      \"location\":\"Maracanã\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b620c3e4b0762325b1c4fc\"\n" +
                                "      },\n" +
                                "      \"matchId\":2,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Internacional\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/internacional_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Sport\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/09/15/sport_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":0,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Ilha do Retiro\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b62116e4b0762325b1c500\"\n" +
                                "      },\n" +
                                "      \"matchId\":3,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Vasco\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/vasco_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Avai\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/avai_60x60_.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":1,\n" +
                                "      \"awayScore\":1,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Ressacada\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b62169e4b0762325b1c501\"\n" +
                                "      },\n" +
                                "      \"matchId\":4,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Chapecoense\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/05/06/chapecoense_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Palmeiras\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/palmeiras_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":4,\n" +
                                "      \"awayScore\":1,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Arena Palmeiras\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b621cee4b0762325b1c505\"\n" +
                                "      },\n" +
                                "      \"matchId\":5,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Atlético Mineiro\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/atletico_mg_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Coritiba\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/coritiba_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":2,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Couto Pereira\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b62217e4b0762325b1c509\"\n" +
                                "      },\n" +
                                "      \"matchId\":6,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Cruzeiro\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/04/29/cruzeiro_65.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Atlético Paranaense\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/06/24/atletico-pr_2015_65.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":1,\n" +
                                "      \"awayScore\":2,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Arena da Baixada\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b62266e4b0762325b1c50b\"\n" +
                                "      },\n" +
                                "      \"matchId\":7,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Flamengo\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/flamengo_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"São Paulo\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/sao_paulo_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":3,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Morumbi\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b622c7e4b0762325b1c513\"\n" +
                                "      },\n" +
                                "      \"matchId\":8,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Ponte Preta\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/ponte_preta_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Corinthians\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/corinthians_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":0,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Itaquerão\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b6232ee4b0762325b1c520\"\n" +
                                "      },\n" +
                                "      \"matchId\":9,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Figueirense\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/figueirense_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Goiás\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/goias_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":1,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Serra Dourada\"\n" +
                                "   },\n" +
                                "   {  \n" +
                                "      \"_id\":{  \n" +
                                "         \"$oid\":\"56b62424e4b0762325b1c533\"\n" +
                                "      },\n" +
                                "      \"matchId\":10,\n" +
                                "      \"awayTeam\":{  \n" +
                                "         \"name\":\"Joinville\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/07/20/Joinville65.png\"\n" +
                                "      },\n" +
                                "      \"homeTeam\":{  \n" +
                                "         \"name\":\"Santos\",\n" +
                                "         \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/santos_60x60.png\"\n" +
                                "      },\n" +
                                "      \"homeScore\":2,\n" +
                                "      \"awayScore\":1,\n" +
                                "      \"date\":\"2015-01-22T19:00:00.000Z\",\n" +
                                "      \"location\":\"Vila Belmiro\"\n" +
                                "   }\n" +
                                "]"));
        server.start();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();

        TestSubscriber<List<Match>> testSubscriber = new TestSubscriber<>();
        retrofit.create(MatchRestService.class).getMatches("MyApiKey").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(10, testSubscriber.getOnNextEvents().get(0).size());
        testSubscriber.assertCompleted();

        RecordedRequest request = server.takeRequest();
        assertEquals("/api/1/databases/heroku_wm3w0h9v/collections/matches?apiKey=MyApiKey", request.getPath());
        assertEquals("GET", request.getMethod());

        server.shutdown();
    }

    @Test public void getLiveTickerEntriesSuccess() throws Exception {
        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("[  \n" +
                        "   {  \n" +
                        "      \"_id\":{  \n" +
                        "         \"$oid\":\"56ba275ee4b0d15f5fdd8dfc\"\n" +
                        "      },\n" +
                        "      \"time\":1,\n" +
                        "      \"matchId\":1,\n" +
                        "      \"description\":\"Começa o jogo!\"\n" +
                        "   },\n" +
                        "   {  \n" +
                        "      \"_id\":{  \n" +
                        "         \"$oid\":\"56ba27a2e4b0d15f5fdd8e1c\"\n" +
                        "      },\n" +
                        "      \"time\":3,\n" +
                        "      \"matchId\":1,\n" +
                        "      \"description\":\"Giuliano fica com a sobra na entrada da área e solta a bomba. A bola vai sobre a meta do Flumninense.\"\n" +
                        "   },\n" +
                        "   {  \n" +
                        "      \"_id\":{  \n" +
                        "         \"$oid\":\"56ba27cee4b0d15f5fdd8e22\"\n" +
                        "      },\n" +
                        "      \"time\":7,\n" +
                        "      \"matchId\":1,\n" +
                        "      \"description\":\"O jogo é todo do Grêmio neste início. O Fluminense se defende como pode.\"\n" +
                        "   }\n" +
                        "]"));
        server.start();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();

        TestSubscriber<List<LiveTickerEntry>> testSubscriber = new TestSubscriber<>();
        retrofit.create(MatchRestService.class).getLiveTickerEntries("{\"matchId\":1}", 0, "MyApiKey").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(3, testSubscriber.getOnNextEvents().get(0).size());
        testSubscriber.assertCompleted();

        RecordedRequest request = server.takeRequest();
        assertEquals("/api/1/databases/heroku_wm3w0h9v/collections/liveTickerEntries?q={%22matchId%22:1}&sk=0&apiKey=MyApiKey", request.getPath());
        assertEquals("GET", request.getMethod());

        server.shutdown();
    }

    @Test public void getMatch() throws Exception {
        final MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{  \n" +
                        "   \"_id\":{  \n" +
                        "      \"$oid\":\"56b53b44e4b0762325b1b64b\"\n" +
                        "   },\n" +
                        "   \"matchId\":1,\n" +
                        "   \"awayTeam\":{  \n" +
                        "      \"name\":\"Grêmio\",\n" +
                        "      \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2014/04/14/gremio_60x60.png\"\n" +
                        "   },\n" +
                        "   \"homeTeam\":{  \n" +
                        "      \"name\":\"Fluminense\",\n" +
                        "      \"icon\":\"http://s.glbimg.com/es/sde/f/equipes/2015/05/05/fluminense-escudo-65x65.png\"\n" +
                        "   },\n" +
                        "   \"homeScore\":5,\n" +
                        "   \"awayScore\":1,\n" +
                        "   \"date\":\"2015-01-22T23:00:00.000Z\",\n" +
                        "   \"location\":\"Maracanã\"\n" +
                        "}"));
        server.start();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(new OkHttpClient())
                .build();

        TestSubscriber<Match> testSubscriber = new TestSubscriber<>();
        retrofit.create(MatchRestService.class).getMatch("{\"matchId\":1}", "MyApiKey").subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValueCount(1);
        assertEquals(1, testSubscriber.getOnNextEvents().get(0).getMatchId());
        assertEquals(5, testSubscriber.getOnNextEvents().get(0).getHomeScore());
        assertEquals(1, testSubscriber.getOnNextEvents().get(0).getAwayScore());
        assertEquals("Maracanã", testSubscriber.getOnNextEvents().get(0).getLocation());
        testSubscriber.assertCompleted();

        RecordedRequest request = server.takeRequest();
        assertEquals("/api/1/databases/heroku_wm3w0h9v/collections/matches?fo=true&q={%22matchId%22:1}&apiKey=MyApiKey", request.getPath());
        assertEquals("GET", request.getMethod());

        server.shutdown();
    }
}