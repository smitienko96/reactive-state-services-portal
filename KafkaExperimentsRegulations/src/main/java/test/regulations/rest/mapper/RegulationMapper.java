package test.regulations.rest.mapper;

import test.regulations.domain.Regulation;
import test.regulations.domain.RegulationTerm;
import test.regulations.domain.published.PublishedRegulation;

import java.util.stream.Collectors;

/**
 *
 */
public class RegulationMapper {

    private RegulationMapper() {
    }

    /**
     * @param regulation
     * @return
     */
    public static PublishedRegulation toPublished(Regulation regulation) {
        PublishedRegulation published = new PublishedRegulation();
        published.setNumber(regulation.identifier().number());
        published.setDate(regulation.identifier().date());

        RegulationTerm regulationTerm = regulation.regulationTerm();
        if (regulationTerm != null) {
            published.setEnactmentDate(regulationTerm.enactmentDate());
            published.setTerminationDate(regulationTerm.terminationDate());
        }

        published.setServiceId(regulation.serviceId().id());
        published.setName(regulation.metadata().name());
        published.setDescription(regulation.metadata().description());
        published.setApplicantTypes(regulation.allowedApplicantTypes().stream()
                .map(t -> t.name()).collect(Collectors.toList()));

        if (regulation.questionary() != null) {
            published.setQuestionaryDocumentType(regulation.questionary().documentTypeId().id());
        }

        return published;
    }
}
