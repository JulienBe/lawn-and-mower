package lam.rest;

import lam.logic.OverallCommander;
import lam.logic.SessionCreatorAutomata;
import lam.records.MowingSession;
import org.jboss.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

@Path("/mow")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.APPLICATION_JSON)
public class InputStringResource {

    private static final Logger LOG = Logger.getLogger(InputStringResource.class);
    private final Set<InputString> inputs = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    @GET
    public String list() {
        LOG.info("Processing " + inputs.size() + " inputs");
        Map<InputString, List<String>> outputs = new LinkedHashMap();
        try {
            for (InputString input : inputs) {
                MowingSession session = new SessionCreatorAutomata().processInput(
                        input.lawn + "\n" +
                                input.mowers.stream().map(m -> m.coord + "\n" + m.instructions).collect(Collectors.joining("\n")));
                outputs.put(input, new OverallCommander(session).exec());
            }
        } catch (Exception e) {
            LOG.error("Exception during processing of stored inputs", e);
            return "Sorry your input could not be processed";
        } finally {
            inputs.clear();
        }
        return outputs
                .entrySet()
                .stream()
                .map(entry -> "Input: \n" + entry.getKey().toString() + "\n\n" + "Output: \n"
                        + entry.getValue().stream()
                            .map(e -> e + "\n")
                            .reduce("", String::concat))
                .collect(Collectors.joining("\n\n"));
    }

    @POST
    public Set<InputString> add(InputString inputString) {
        inputs.add(inputString);
        return inputs;
    }

}
