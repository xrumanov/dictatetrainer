package cz.muni.fi.dictatetrainer.corrector.resource;

import cz.muni.fi.dictatetrainer.corrector.resource.common.model.HttpCode;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CorrectorResourceUnitTest {

    private CorrectorResource correctorResource = new CorrectorResource();

    @Test
    public void correctTextValidInput() {
        final String request  = "{\"dictateTranscript\":\"Som dobrý.\",\"userText\":\"Som Dobrí.\"}";

        final Response response = correctorResource.correct(request);
        System.out.println(response.getEntity().toString());
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

    }

    @Test
    public void correctTextDictateTranscriptMissing(){

    }

    @Test
    public void correctTextUserTextMissing(){

    }
}
