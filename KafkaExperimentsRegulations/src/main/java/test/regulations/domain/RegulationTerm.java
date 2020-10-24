package test.regulations.domain;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 */
public class RegulationTerm {

    private Date enactmentDate;
    private Date terminationDate;

    public RegulationTerm(@NotNull Date enactmentDate) {
        this(enactmentDate, null);
    }

    public RegulationTerm(@NotNull Date enactmentDate, Date terminationDate) {
        this.enactmentDate = enactmentDate;
        this.terminationDate = terminationDate;
    }

    public Date enactmentDate() {
        return new Date(enactmentDate.getTime());
    }

    public Date terminationDate() {
        return terminationDate != null ? new Date(terminationDate.getTime()) : null;
    }

    public RegulationTerm withTermination(Date terminationDate) {
        return new RegulationTerm(this.enactmentDate, terminationDate);
    }

}
