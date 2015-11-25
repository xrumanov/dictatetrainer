package cz.muni.fi.dictatetrainer.corrector.rules.impl;

import cz.muni.fi.dictatetrainer.corrector.model.Mistake;
import cz.muni.fi.dictatetrainer.corrector.model.MistakeType;
import cz.muni.fi.dictatetrainer.corrector.rules.CorrectorRules;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of non-contextual corrector rules
 * Only shows description for mistakes that are non-contextual (without a context of by-standing words)
 * <p> TODO how to choose priority number
 * TODO consider moving definitions somewhere else to not have that much data in mistake
 */
public class CorrectorRulesNoContext implements CorrectorRules {

    public CorrectorRulesNoContext() {

    }

    @Override
    public Mistake applyRules(Mistake mistake) {

        String mostGeneralRule = "Pro tuto chybu zatím nemáme vysvetlení.";
        String description;

        char[] precedingCharacters = new char[]{'b', 'l', 'm', 'p', 's', 'v', 'z'};


        for (char precedingCharacter : precedingCharacters) { // vyjmenovana slova

            if ((description = vyjmenovaneSlovoDubletOrNull(mistake, precedingCharacter)) != null ||
                    (description = vyjmenovaneSlovoOrNull(mistake, precedingCharacter)) != null) {
                mistake.setMistakeType(MistakeType.VYJMENOVANA_SLOVA);
                mistake.setMistakeDescription(description);
                mistake.setPriority(10);
                return mistake;
            }
        }

        if ((description = iyPoPismenuCOrNull(mistake)) != null) { // Psaní i/y po písmenu c
            mistake.setMistakeType(MistakeType.IY_po_C);
            mistake.setMistakeDescription(description);
            mistake.setPriority(10);
            return mistake;

        } else if ((description = predponySZOrNull(mistake)) != null) { // Psaní předpon s-, z-
            mistake.setMistakeType(MistakeType.PREDPONY_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = predlozkySZOrNull(mistake)) != null) { // Psaní předložek s, z (z postele, s knihou, s sebou)
            mistake.setMistakeType(MistakeType.PREDLOZKY_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = prejataSlovaSZOrNull(mistake)) != null) { // Pravopis a výslovnost přejatých slov se s – z
            mistake.setMistakeType(MistakeType.PREJATA_SLOVA_S_Z);
            mistake.setMistakeDescription(description);
            mistake.setPriority(7);
            return mistake;

        } else if ((description = psaniDisDysOrNull(mistake)) != null) { // Psaní dis-, dys-
            mistake.setMistakeType(MistakeType.DIS_DYS);
            mistake.setMistakeDescription(description);
            mistake.setPriority(8);
            return mistake;

        } else if ((description = psaniSeZakoncenimManieOrNull(mistake)) != null) { // Psaní slov se zakončením -manie
            mistake.setMistakeType(MistakeType.SLOVA_ZAKONCENE_MANIE);
            mistake.setMistakeDescription(description);
            mistake.setPriority(6);
            return mistake;

        } else if ((description = psaniNnNOrNull(mistake)) != null) { // Psaní n – nn
            mistake.setMistakeType(MistakeType.PSANI_N_NN);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = psaniSprezekSprahovaniOrNull(mistake)) != null) { // Psaní spřežek a spřahování
            mistake.setMistakeType(MistakeType.SPREZKY_SPRAHOVANI);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = slozenaPridavniJmenaOrNull(mistake)) != null) { //Složená přídavná jména - První část přídavného jména je zakončena na -sko, -cko, -ně nebo –ově
            mistake.setMistakeType(MistakeType.SLOZENA_ADJEKTIVA);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = velkaPismenaOrNull(mistake)) != null) { // Psaní spřežek a spřahování
            mistake.setMistakeType(MistakeType.VELKA_PISMENA);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;
        } else if ((description = adjZakoncenaIciOrNull(mistake)) != null) { // Přídavná jména zakončená na -icí – -ící
            mistake.setMistakeType(MistakeType.ADJEKTIVA_ICI);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = adjZakoncenaNiOrNull(mistake)) != null) { // Přídavná jména zakončená na -ní – -ný
            mistake.setMistakeType(MistakeType.ADJEKTIVA_NI_NY);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = typAckoliAckolivOrNull(mistake)) != null) { // Typ ačkoli – ačkoliv, kdokoli – kdokoliv
            mistake.setMistakeType(MistakeType.TYP_ACKOLI_ACKOLIV);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = vokalizacePredlozekOrNull(mistake)) != null) { // Vokalizace předložek
            mistake.setMistakeType(MistakeType.VOKALIZACE_PREDLOZEK);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = zajmenaVasiJiNiOrNull(mistake)) != null) { // Zájmena typu vaši/vaší, ji/jí, ni/ní
            mistake.setMistakeType(MistakeType.ZAJMENA_VASI_JI_NI);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = psaniBeBjeVeVjePeOrNull(mistake)) != null) { // Psaní bě/bje, vě/vje, pě
            mistake.setMistakeType(MistakeType.PSANI_BE_VE_PE);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = souhlaskyParoveOrNull(mistake)) != null) { // Souhlásky párové
            mistake.setMistakeType(MistakeType.SOUHLASKY_PAROVE);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = diakritikaOrNull(mistake)) != null) { // Diakritika
            mistake.setMistakeType(MistakeType.DIAKRITIKA);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = iPoMekkychaObojetnychSouhlaskachOrNull(mistake)) != null) { // i/í po měkkých a obojetných souhláskách
            mistake.setMistakeType(MistakeType.I_PO_MEKKYCH_OBOJETNYCH_SOUHLASKACH);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else if ((description = zajmenaMneMeASlovaJeObsahujiciOrNull(mistake)) != null) { // zájmena mně x mě a slova obsahující mě a mně
            mistake.setMistakeType(MistakeType.ZAJMENA_A_SLOVA_OBSAHUJICI_MNE_ME);
            mistake.setMistakeDescription(description);
            mistake.setPriority(5);
            return mistake;

        } else {
            mistake.setMistakeType(MistakeType.OSTATNI); // mistake is not found
            mistake.setMistakeDescription(mostGeneralRule);
            mistake.setPriority(0);
            return mistake;
        }
    }

    //-------------Pravopis----------------

    private String vyjmenovaneSlovoDubletOrNull(Mistake mistake, char precededChar) {

        String tag = getTag(mistake);
        String lemma = getLemma(mistake);
        String lemmaWithoutAccentsOnIY = getLemmaWithoutAccentsOnIY(mistake);
        String writtenWord = getWrittenWord(mistake);
        Integer mistakeCharPosition = getMistakeCharPositionInWord(mistake);
        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);

        Map<String, String> vyjmenovanaSlovaDublet;
        List<Character> iy = CorrectorRulesStaticLists.getIY();

        if (mistakeCharPosition < 1) return null;

        switch (precededChar) {
            case 'b':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoBDublet();
                break;
            case 'l':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoLDublet();
                break;
            case 'm':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoMDublet();
                break;
            case 'p':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoPDublet();
                break;
            case 's':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoSDublet();
                break;
            case 'v':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoVDublet();
                break;
            case 'z':
                vyjmenovanaSlovaDublet = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoZDublet();
                break;
            default:
                return null; //invalid character - not supported
        }

        Set<String> keySet = vyjmenovanaSlovaDublet.keySet();

        if (((correctChars.equals("y") && writtenChars.equals("i"))
                || (correctChars.equals("ý") && writtenChars.equals("í"))
                || (correctChars.equals("i") && writtenChars.equals("y"))
                || (correctChars.equals("í") && writtenChars.equals("ý")))
                && (((!tag.startsWith("k2")) && vyjmenovanaSlovaDublet.containsKey(lemma))
                || (tag.startsWith("k2") && (prefixMatchesSet(lemmaWithoutAccentsOnIY, keySet) != null)
                && mistakeCharPosition != mistake.getCorrectWord().length()))
                && (iy.contains(lemma.charAt(mistakeCharPosition - 1)))
                && writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2))
                && lemma.charAt(mistakeCharPosition - 2) == precededChar) {

            if (precededChar == 'b') {
                String definition = "Ke slovesům být (= existovat) i bít (= tlouct) tvoříme řadu předponových sloves " +
                        "(popř. přídavných jmen). Rozlišujeme: ";
                return definition + " " + vyjmenovanaSlovaDublet.get(lemma);
            } else {
                return vyjmenovanaSlovaDublet.get(lemma);
            }
        }
        return null;
    }

    private String vyjmenovaneSlovoOrNull(Mistake mistake, char precededChar) {

        String lemma = getLemma(mistake);
        String tag = getTag(mistake);
        Integer mistakeCharPosition = getMistakeCharPositionInWord(mistake);
        String writtenWord = getWrittenWord(mistake);
        char correctChars;
        char writtenChars;
        if (mistake.getCorrectChars() != "" && mistake.getWrittenChars() != "") {
            correctChars = mistake.getCorrectChars().toLowerCase().toCharArray()[0];
            writtenChars = mistake.getWrittenChars().toLowerCase().toCharArray()[0];
        } else {
            return null;
        }

        Map<String, String> vyjmenovanaSlova;

        if (mistakeCharPosition < 1) return null;

        String generalDefinition = "Jde o vyjmenované nebo z cizího jazyka přejaté slovo. " +
                "Tato slova mají společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), " +
                "které jsou tzv. obojetné, a ke každému ze zmíněných písmen existuje seznam výjimek, " +
                "u nichž se na místo i/í píše y/ý jako v tomto případě. Důvod je historický a u každého slova jiný, " +
                "je nutné si jej pamatovat. (VOJ)";

        if (((correctChars == 'y' && writtenChars == 'i'))
                || (correctChars == 'ý' && writtenChars == 'í')) {

            if (lemma.startsWith("fyto")) {

                return "Slova s předponou fyto- patří mezi vyjmenovaná slova po f. " + generalDefinition;

            } else if (lemma.startsWith("vý") || lemma.startsWith("vy")) {

                return "Slova s předponou vy-/vý- patří mezi vyjmenovaná slova po v. " + generalDefinition;
            } else {

                switch (precededChar) {
                    case 'b':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoB();
                        break;
                    case 'l':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoL();
                        break;
                    case 'm':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoM();
                        break;
                    case 'p':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoP();
                        break;
                    case 's':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoS();
                        break;
                    case 'v':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoV();
                        break;
                    case 'z':
                        vyjmenovanaSlova = CorrectorRulesStaticLists.getVyjmenovanaSlovaPoZ();
                        break;
                    default:
                        return null; //invalid character - not supported
                }

                if (lemma.charAt(mistakeCharPosition - 2) == precededChar && vyjmenovanaSlova.containsKey(lemma)
                        && ((writtenWord.charAt(mistakeCharPosition - 1) == writtenChars) && (lemma.charAt(mistakeCharPosition - 1)) == correctChars)
                        && (writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2)))
                        && (!tag.startsWith("k2") || tag.startsWith("k2") && mistakeCharPosition != mistake.getCorrectWord().length())) {

                    return vyjmenovanaSlova.get(lemma) + " " + generalDefinition;

                }
            }

        }
        return null;
    }

    private String iyPoPismenuCOrNull(Mistake mistake) {

        String lemma = getLemma(mistake);
        String tag = getTag(mistake);
        Integer mistakeCharPosition = getMistakeCharPositionInWord(mistake);
        String writtenWord = getWrittenWord(mistake);
        char correctChars;
        char writtenChars;
        if (mistake.getCorrectChars() != "" && mistake.getWrittenChars() != "") {
            correctChars = mistake.getCorrectChars().toLowerCase().toCharArray()[0];
            writtenChars = mistake.getWrittenChars().toLowerCase().toCharArray()[0];
        } else {
            return null;
        }


        if (mistakeCharPosition < 1) return null;

        if (((((correctChars == 'y') && (writtenChars == 'i'))
                || ((correctChars == 'ý') && (writtenChars == 'í'))
                || ((correctChars == 'i') && writtenChars == 'y'))
                || ((correctChars == 'í') && (writtenChars == 'ý')))
                && lemma.charAt(mistakeCharPosition - 2) == 'c'
                && ((writtenWord.charAt(mistakeCharPosition - 1) == writtenChars) && (lemma.charAt(mistakeCharPosition - 1)) == correctChars)
                && (writtenWord.charAt(mistakeCharPosition - 2) == (lemma.charAt(mistakeCharPosition - 2)))
                && (!tag.startsWith("k2") || tag.startsWith("k2") && mistakeCharPosition != mistake.getCorrectWord().length())) {

            return "Po c se v domácích slovech píše měkké i (např. cit, církev atd.). " +
                    "U cizích slov pravidlo o tom, že se po c píše měkké i, neplatí. Na jedné straně máme " +
                    "slova jako citron, nacista atp., na straně druhé máme slova, v nichž se po c píše tvrdé y – " +
                    "např. cyklon, cynik apod. Pro psaní i a y v těchto slovech neexistuje žádné pravidlo, musíme si " +
                    "pamatovat jednotlivé příklady. (IJP)";
        }
        return null;
    }

    private String predponySZOrNull(Mistake mistake) {

        Integer mistakeCharPosition = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);
        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);

        List<String> predponySZ = CorrectorRulesStaticLists.getPredponySZ();
        Map<String, String> predponySZdublet = CorrectorRulesStaticLists.getPredponySZDublet();

        if (mistakeCharPosition < 1) return null;

        if (mistakeCharPosition == 1 && ((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s"))) && (lemma.length() > 2)) {
            if (predponySZ.contains(lemma)) {
                return "NECHYBA U skupiny sloves mezi podobami s předponou s- a z- zásadní " +
                        "významový rozdíl není, a můžete je psát oběma způsoby: " +
                        "zcestovat i scestovat, zkrápět i skrápět... (IJP)";

            } else if (predponySZdublet.containsKey(lemma)) {
                String definition = "Některá slova lze psát jak s předponou s-, tak s předponou z-. " +
                        "U většiny z nich je mezi oběma podobami zřetelný významový rozdíl. (IJP)";

                return definition + " " + predponySZdublet.get(lemma);
            } else if (writtenChars.equals("s") && lemma.endsWith("ovat")) {
                return "U sloves zakončených na -ovat (aktualizovat, kopírovat) je podoba " +
                        "s předponou z- vždy správná. Jen u některých z nich lze zvolit i předponu s- " +
                        "(zestylizovat i sestylizovat, zkontaktovat i skontaktovat). " +
                        "Výjimkou jsou pouze ta slovesa, která mají s- už v původním jazyku: " +
                        "skandalizovat (neexistuje samotné kandalizovat), skandovat. (IJP)";

            } else {
                return "Předpona s(e)- naznačuje " +
                        "a) směřování dohromady: scelit, shromáždit se, spojit; " +
                        "b) směřování shora dolů, po povrchu pryč: sklonit, sklopit, shýbnout se; " +
                        "c) zmenšení objemu, až zánik (scvrknout se, shořet). " +
                        "U mnohých slov je třeba si způsob psaní pamatovat, neboť původní význam si " +
                        "dnes již neuvědomujeme: skončit, slevit (sleva)." +
                        "Předponou z(e)- se tvoří: " +
                        "a) od nedokonavých sloves dokonavá, která nemají žádný z významů (moknout › zmoknout); " +
                        "b) od podstatných nebo přídavných jmen slovesa mající význam " +
                        "‚učinit nebo stát se tím, co znamená slovo základové‘: ocel › zocelit. " +
                        "U mnohých slov si je třeba způsob psaní pamatovat: zkoumat, zkoušet (IJP)";
            }
        }
        return null;
    }

    private String predlozkySZOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        int correctWordLength = getCorrectWordLength(mistake);

        if (((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s")))
                && ((correctWordLength == 1) || (correctWordLength == 2 && writtenChars.charAt(1) == 'e'))) {
            return "S druhým pádem se pojí předložka z (ze). Např. vstal z postele; vyšel z banky. " +
                    "Se sedmým pádem se pojí jedině předložka s (se). " +
                    "Např. trávil čas s knihou; s pilinami si lze hrát. (IJP)";

        }
        return null;
    }

    private String prejataSlovaSZOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        Integer mistakeCharPosition = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);

        List<String> prejataSlovaSZ = CorrectorRulesStaticLists.getPrejataSlovaSZ();
        List<String> prejateSlovaSZDublet = CorrectorRulesStaticLists.getPrejataSlovaSZDvojice();
        List<String> prejataSlovaSZizmus = CorrectorRulesStaticLists.getPrejataSlovaSZIzmusZmus();

        if (((correctChars.equals("s") && writtenChars.equals("z"))
                || (correctChars.equals("z") && writtenChars.equals("s")))
                && mistakeCharPosition != 1) {
            if (prejataSlovaSZ.contains(lemma)) {

                return "NECHYBA Ve slovech zakončených v 1. pádě " +
                        "na skupinu vyslovovanou [-ns, -rs, -ls, např. kurs], v ostatních pádech " +
                        "a ve slovech odvozených vyslovovanou [-nz-, -rz-, -lz-, např. bez kurzu], " +
                        "jsou obě možnosti psaní rovnocenné, a to bez stylového rozlišení. (IJP)";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat

            } else if (prejateSlovaSZDublet.contains(lemma)) {

                return "Ve slovech zdomácnělých, kde se původní s vždy v češtině vyslovuje jako [z], " +
                        "se podoby se z považují za základní, tedy stylově neutrální. " +
                        "Podoby se s jsou stylově příznakové, proto se užívají ve specifických " +
                        "(např. úzce odborných) textech. Např.: analýza – analysa, azyl – asyl";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat

            } else if (prejataSlovaSZizmus.contains(lemma)) {

                return "Ve slovech s příponou vyslovovanou jako [-izmus]/[-zmus] a [-zmus]/[-zma] " +
                        "můžeme psát s i z, přičemž podoba se s se považuje za základní. (IJP)";
                // TODO AND další znakovou neshodu v chybném lemmatu, která se týká kvantity vokálů, ignorovat
            }
        }
        return null;
    }

    private String psaniDisDysOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        String lemma = getLemma(mistake);

        Map<String, String> psaniDisDys = CorrectorRulesStaticLists.getDisDys();

// TODO chyba je přítomna i v lemmatickém tvaru AND lemma chybného slova je přítomno v seznamu

        if (((correctChars.equals("y") && writtenChars.equals("i")
                || (correctChars.equals("i") && writtenChars.equals("y")))
                && (correctWord.startsWith("dis") || correctWord.startsWith("dys"))
                && (psaniDisDys.containsKey(lemma)))) {
            String definition = "Významově poměrně blízké předpony dis- a dys- mají rozdílný původ. " +
                    "O jejich užití obvykle rozhoduje tradice. Latinské dis- odpovídá české předponě roz-, " +
                    "popř. ne-. Řecké dys- znamená ‚zeslabený, vadný, porušený‘, " +
                    "předpona vyjadřuje něco negativního, špatného. (IJP)";

            return definition + " " + psaniDisDys.get(lemma);
        }
        return null;
    }

    private String psaniSeZakoncenimManieOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String lemma = getLemma(mistake);

        if ((correctChars.equals("a") && writtenChars.equals("á"))
                || (correctChars.equals("á") && writtenChars.equals("a"))) {

            if ((lemma.endsWith("manie") || lemma.endsWith("mánie")) && lemma.length() > 5) {

                String definition = "NECHYBA Ve složeninách obsahujících -manie je možné psát -mánie i -manie. (VOJ)";

                return definition;

            } else if (lemma.equals("mánie")) {
                return "Přípustná je pouze varianta mánie. " +
                        "Pouze pokud jde o slovo složené (např. cestománie, pokemanie) " +
                        "je možné psát krátce i dlouze. (VOJ)";

            }
        }
        return null;
    }

    private String psaniNnNOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);

        if (mistakeCharPosInWord < 1) return null;

        if ((correctChars.equals("n") && writtenChars.equals(""))
                || (correctChars.equals("") && writtenChars.equals("n"))
                && (mistakeCharPosInWord != 1) &&
                ((correctWord.charAt(mistakeCharPosInWord) == 'n') || (correctWord.charAt(mistakeCharPosInWord - 2)) == 'n')) {

            return "Dvě n se píšou u přídavných jmen odvozených příponou -ní, -ný od podstatných jmen, " +
                    "jejichž kořen končí na -n,, -ň (den – denní, holeň – holenní). Dvě n se píšou " +
                    "i u přídavných jmen (a u výrazů od nich odvozených), u nichž výchozí podstatné jméno neexistuje " +
                    "nebo se neužívá, ale kořen je zakončen na -n (bezcenný (bezcennost)). Píše se také v druhých a " +
                    "třetích stupních přídavných jmen a slovech od nich odvozených." +
                    "Jedno n se píše v případech: a) u přídavných jmen odvozených příponou -í od jmen pojmenovávajících " +
                    "zvířata, jejichž kořen končí na -n: (havran – havraní), a od příčestí trpných: dán – daný; " +
                    "b) slovo raný ve významu ,brzký, časný‘ (raná gotika) a od r. 1957 též dceřiný; " +
                    "c) u přídavných jmen utvořených od podstatných jmen příponou -ěný: vlna – vlněný (stejně jako sláma – slaměný);" +
                    "d) podstatná jména odvozená od přídavných jmen na -ní, -ný příponami -ík, -ice, -ina: deník, okenice, cenina. (IJP)";

        }
        return null;
    }

    private String psaniSprezekSprahovaniOrNull(Mistake mistake) {

        String correctWord = getCorrectWord(mistake);
        String writtenWord = getWrittenWord(mistake);

        List<String> sprezkySeznam = CorrectorRulesStaticLists.getSprezkyASprahovani();

        if ((writtenWord.equals("zato") && (correctWord.equals("za to"))) ||
                (writtenWord.equals("za to") && (correctWord.equals("zato")))) {

            return "Příslovce zato užíváme ve významu ‚náhradou za něco, místo něčeho‘ " +
                    "(Vrátil se s nepořízenou, zato rozčílený.). Zato také funguje ve významu spojky odporovací, " +
                    "synonymickým výrazem je však: Učit se nechtěl, zato se uměl prát. " +
                    "V ostatních případech píšeme výraz za to zvlášť: Mám za to, že je schůze už zbytečně dlouhá. (IJP)";

        } else if ((writtenWord.equals("nato") && (correctWord.equals("na to"))) ||
                (writtenWord.equals("na to") && (correctWord.equals("nato")))) {

            return "Příslovce nato znamená ‚potom, poté‘: Vypil láhev vitriolu, brzy nato zemřel. " +
                    "Hned nato začala předstírat, že se nic neděje. " +
                    "Zvlášť píšeme spojení předložky a zájmena: Jdeme na to. (IJP)";

        } else if ((writtenWord.equals("přitom") && (correctWord.equals("pŕi tom")))
                || (writtenWord.equals("pŕi tom") && (correctWord.equals("přitom")))
                || (writtenWord.equals("potom") && (correctWord.equals("po tom")))
                || (writtenWord.equals("po tom") && (correctWord.equals("potom")))) {

            return "Zde není vymezení tak jednoznačné. Oslabuje se totiž chápání tohoto výrazu jako spojení předložky " +
                    "a ukazovacího zájmena a ve většině textů lze volbu odůvodnit jak významem ‚zároveň‘ (přitom), " +
                    "tak vztáhnutím na děj předchozí věty, ke kterému zájmeno odkazuje (při tom)." +
                    "Podobný případ je předložka + zájmeno po tom a příslovce potom. " +
                    "Hrál a potom zpíval (‚následovně‘). Hrál a po tom zpíval. " +
                    "(= Po hraní na hudební nástroj začal zpívat.) (IJP)";

        } else if ((writtenWord.equals("po té") && (correctWord.equals("poté"))) ||
                (writtenWord.equals("poté") && (correctWord.equals("po té")))) {

            return "Pokud ukazovací zájmeno odkazuje k entitě ženského rodu, jsou ve většině případů možné obě varianty: " +
                    "Jako předkrm si dáme šunku a po té (‚po šunce‘) hlavní chod. " +
                    "Jako předkrm si dáme šunku a poté (‚následovně‘) hlavní chod. " +
                    "Kde však tato situace nenastává, tj. pokud odkazujeme k jinému rodu, " +
                    "je možná pouze jednoslovná podoba – Dáme si čaj a poté (v časovém významu – " +
                    "‚až dopijeme čaj‘) půjdeme spát. (IJP)";

        } else if (sprezkySeznam.contains(correctWord)) {

            return "Zvlášť píšeme zpodstatnělá přídavná jména, která jsou užívaná pouze či převážně s předložkou na a " +
                    "pojí se s 4. pádem. Jsou to spojení jako: na shledanou, na viděnou apod. (IJP)";
        }

        return null;
    }

    private String slozenaPridavniJmenaOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);

        if (mistakeCharPosInWord < 1) return null;

        if ((correctChars.equals("-") && writtenChars.equals(""))
                && (mistakeCharPosInWord != 1) && (mistakeCharPosInWord != correctWord.length())
                && (correctWord.contains("sko-") || correctWord.contains("cko-")
                || correctWord.contains("ně-") || correctWord.contains("ově-"))) {

            return "V těchto případech používáme pro oddělení obou složek spojovník " +
                    "(ten zde v podstatě plní funkci spojky a). " +
                    "Např.: zemědělsko-potravinářský (týkající se zemědělství a potravinářství). " +
                    "Někdy je užíván k rozlišení: politicko-ekonomický (souřadicí; týkající se politiky a ekonomie) " +
                    "a politickoekonomický (podřadicí; týkající se politické ekonomie) (IJP)";
        }
        return null;
    }

    private String velkaPismenaOrNull(Mistake mistake) {

        String correctChars = mistake.getCorrectChars();
        String writtenChars = mistake.getWrittenChars();
        Integer wordPosition = getWordPosition(mistake);
        String previousWord = getPreviousWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);

        if (mistakeCharPosInWord < 1) return null;

        if ((correctChars.length() == 1 && writtenChars.length() == 1)
                && (Character.isLowerCase(correctChars.toCharArray()[0])
                && Character.isUpperCase(writtenChars.toCharArray()[0])
                || (Character.isUpperCase(correctChars.toCharArray()[0]))
                && (Character.isLowerCase(writtenChars.toCharArray()[0])))
                && writtenChars.toLowerCase().equals(correctChars.toLowerCase())) {

            if (previousWord.endsWith(".") || previousWord.endsWith("!")
                    || previousWord.endsWith("?") || wordPosition == 1) {
                String definition = "Věta vždy začíná velkým písmenem.";

                return definition;

            } else if (mistakeCharPosInWord == 1) {
                return "Názvy jednoslovné (např. jméno Miloš, město Kladno) začínají velkým písmenem. " +
                        "U víceslovných názvů (např. Mrtvé moře) se píše jako velké pouze první písmeno, pokud už " +
                        "neobsahují další název (např. Ústí nad Labem, řeka Labe). K těmto základním pravidlům však " +
                        "existuje množství dodatků (k nalezení např. v Pravidlech českého pravopisu), " +
                        "např. o psaní názvů ulic, živých bytostí, institucí atd. (VOJ)";

            }
        }
        return null;
    }

    //------------Slovotvorba-------------------

    private String adjZakoncenaIciOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);

        if (((correctChars.equals("i") && writtenChars.equals("í"))
                || (correctChars.equals("í") && writtenChars.equals("i")))
                && (lemma.endsWith("icí") || lemma.endsWith("ící"))
                && mistakeCharPosInWord == correctWord.length() - 2) {

            return "Podoby typu měřicí, řídicí jsou přídavná jména účelová. Vyjadřují, k čemu věc slouží: " +
                    "měřicí zařízení je zařízení určené k měření, řídicí (ovládací) panel je pojmenován podle toho, " +
                    "že slouží k řízení (ovládání). Přídavná jména typu měřící, řídící se nazývají dějová. " +
                    "Jimi se vyjadřuje děj (činnost) právě probíhající: např. řídící žena je žena, která právě řídí. " +
                    "U osob se podoby na -ící používají výhradně: řídící pracovník, velící důstojník, místodržící." +
                    "Pomůcka: Do daného spojení dosadit jeden člen z dvojic typu: vzdělávací – vzdělávající. " +
                    "Je-li vzdělávací středisko, mělo by být i školicí středisko, je-li útvar provádějící činnost, " +
                    "pak je i útvar řídící činnost. (IJP)";
        }
        return null;
    }

    private String adjZakoncenaNiOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);

        Map<String, String> adjektivaZakoncenaNiNyList = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNy();
        Map<String, String> adjektivaZakoncenaNiNyVyznamoveTotozneList
                = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNyVyznamoveTotozne();
        Map<String, String> adjektivaZakoncenaNiNyJineZakladyList
                = CorrectorRulesStaticLists.getAdjektivaZakoncenaNiNyJineZaklady();


        if ((correctChars.equals("í") && writtenChars.equals("ý"))
                || (correctChars.equals("ý") && writtenChars.equals("í"))
                && ((mistakeCharPosInWord == correctWord.length())
                || (mistakeCharPosInWord == correctWord.length() - 1)
                && ((correctWord.endsWith(".")) || ((correctWord.endsWith(".")) || ((correctWord.endsWith("."))))))
                && (correctWord.charAt(mistakeCharPosInWord - 2) == 'n')) {

            if (adjektivaZakoncenaNiNyList.containsKey(lemma)) {
                String definition = "Nevelká skupina jmen, u nichž si -ní a -ný buď konkurují, nebo slouží k rozlišení " +
                        "různých významů. Zakončení na -ný mají přídavná jména s významem hodnotícím, proto " +
                        "např. spojením zábavný pořad vyjadřujeme svůj názor na charakter pořadu, hodnotíme jej, " +
                        "kdežto jako zábavní pořad může být tvůrci označen i pořad, který diváci po zhlédnutí možná " +
                        "ani za zábavný považovat nebudou. (IJP)";
                return definition + " " + adjektivaZakoncenaNiNyList.get(lemma);

            } else if (adjektivaZakoncenaNiNyVyznamoveTotozneList.containsKey(lemma)) {
                String definition = "NECHYBA Významově totožné varianty.";
                return definition + " " + adjektivaZakoncenaNiNyVyznamoveTotozneList.get(lemma);

            } else if (adjektivaZakoncenaNiNyJineZakladyList.containsKey(lemma)) {
                String definition = "Tato jména jsou tvořena od různých základů slov.";

                return definition + adjektivaZakoncenaNiNyJineZakladyList.get(lemma);
            }
        }
        return null;
    }

    private String typAckoliAckolivOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        String tag = getTag(mistake);

        if ((correctChars.equals("v") && writtenChars.equals(""))
                || (correctChars.equals("") && writtenChars.equals("v"))
                && (correctWord.length() == mistakeCharPosInWord)
                && (correctWord.endsWith("koli")) || (correctWord.endsWith("koliv"))
                && tag.startsWith("k1")) {

            return "NECHYBA Zájmena jako např. kdokoli(v), cokoli(v) atd., příslovce kdykoli(v), " +
                    "kamkoli(v), částice nikoli(v) a spojka ačkoli(v) " +
                    "mohou mít dvě zcela rovnocenné podoby zakončení: -koli a -koliv. (IJP)";

        }
        return null;
    }

    private String vokalizacePredlozekOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        String nextWord = getNextWord(mistake);
        char precedingChar = getPrecedingChar(mistake);

        List<Character> vokaly = CorrectorRulesStaticLists.getVOKALY();
        List<Character> konsonanty = CorrectorRulesStaticLists.getKONSONANTY();


        if ((writtenChars.equals("") && correctChars.equals("e"))) {

            if ((correctWord.length() == 2)
                    && (precedingChar == 'k' || precedingChar == 's' || precedingChar == 'v' || precedingChar == 'z')) {
                if (precedingChar == nextWord.charAt(0)) {
                    return "Před slovem, které začíná stejnou souhláskou jako předložka, " +
                            "vokalizujeme vždy. Např.: ke kořenům, se sestrou. (IJP)";
                } else if (((precedingChar == 's')
                        && ((nextWord.startsWith("z")) || (nextWord.startsWith("ž")) || (nextWord.startsWith("š"))))
                        || ((precedingChar == 'z')
                        && ((nextWord.startsWith("s")) || (nextWord.startsWith("š")) || (nextWord.startsWith("ž"))))
                        || ((precedingChar == 'v')
                        && ((nextWord.startsWith("f"))))
                        || ((precedingChar == 'k')
                        && ((nextWord.startsWith("g"))))) {

                    return "Tendence k vokalizaci je silná, pokud předložka stojí před slovem začínajícím podobnou " +
                            "souhláskou, tedy s před z-, ž-, š-; z před s-, š-, ž-; v před f-; k před g-. " +
                            "Např.: ke groši, se zemí, ve Finsku. U některých slov však pravidlo nemusí " +
                            "platit (Např.: nevycházím z šoku). Důležitou roli zde hraje právě faktor výslovnosti. (IJP)";
                }
            }
        } else if ((writtenChars.equals("e") && correctChars.equals(""))) {
            if ((correctWord.length() == 1)
                    && (correctWord.equals("k") || correctWord.equals("s") || correctWord.equals("v") || correctWord.equals("z"))) {
                if (vokaly.contains(nextWord.charAt(0))) {
                    return "Před slovem začínajícím samohláskou se neslabičné předložky nevokalizují " +
                            "(z \"k\" se nestane \"ke\" atp.). Např.: k ústavě, s ementálem. (IJP)";

                } else if ((correctWord.equals("s")) && (nextWord.equals("sebou"))) {
                    return "Výjimka, jde o ustálené spojení. Vyskytuje se pouze v instrumentálu. " +
                            "Např. brát s sebou, vzít s sebou. Sebou vyjadřuje pohyb těla (mrskat sebou, …). (IJP)";
                } else if ((konsonanty.contains(nextWord.charAt(0))) && (vokaly.contains(nextWord.charAt(1)))
                        || (nextWord.startsWith("ch") && (vokaly.contains(nextWord.charAt(2))))) {
                    return "Před jednou souhláskou se neslabičné předložky nevokalizují. Např.: k řece, s pentličkami. (IJP)";
                }
            }
        } else if (((writtenChars.equals("u") && correctChars.equals(""))
                || (writtenChars.equals("") && correctChars.equals("u")))
                && (precedingChar == 'k')
                && ((correctWord.length() == 1) || (correctWord.length() == 2))) {
            return "Ku se dochovalo pouze v některých ustálených spojeních, " +
                    "a to jen před slovy začínajícími retnou souhláskou, zejména p " +
                    "(např. ku příkladu, ku prospěchu) a ve vyjadřování poměrnosti (např. pět ku sedmi). (IJP)";
        }

        return null;
    }

    private String zajmenaVasiJiNiOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);

        if (((writtenChars.equals("i") && correctChars.equals("í"))
                || (writtenChars.equals("í") && correctChars.equals("i"))) &&
                (((getCorrectWordLength(mistake) == 2) && ((correctWord.startsWith("z")) || (correctWord.startsWith("n"))))
                        || ((correctWord.length() == 4) && ((correctWord.startsWith("vaš")) || (correctWord.startsWith("naš")))))) {

            return "Krátké varianty ji/ni, vaši/naši apod. jsou vázány pouze ke 4. pádu, " +
                    "ostatní pády jsou jí/ní vaší/naší. Pomůcka: Za dané zájmeno dosadit " +
                    "ukazovací zájmena té, tou a tu. Pokud se do věty hodí zájmeno tu, patří sem ni, ji " +
                    "(Nestojím o ni. -> Nestojím o tu dívku.). " +
                    "V ostatních případech (té, tou) sem patří jí, ní (Šli bez ní. -> Šli bez té dívky.). (VOJ)";
        }
        return null;
    }

    private String psaniBeBjeVeVjePeOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        char precedingChar = getPrecedingChar(mistake);

        if ((writtenChars.equals("ě") && correctChars.equals("je"))
                || (writtenChars.equals("je") && correctChars.equals("ě"))
                && ((precedingChar == 'b') || (precedingChar == 'v') || (precedingChar == 'p'))) {

            return "Skupiny bje a vje píšeme u těch slov, která jsou tvořena předponami ob- a v-, " +
                    "přičemž kořen slova začíná na je-. (ob-jezdit – objezd; v-jet – vjezd). " +
                    "U ostatních slov píšeme ě (oběd, pěnkava, větev). V žádném českém slovu se nevyskytuje pjě. " +
                    "Řídkou výjimkou jsou některé názvy, např. Skopje. (VOJ)";

        }
        return null;
    }

    private String souhlaskyParoveOrNull(Mistake mistake) {

        char correctCharsFirstChar = getCorrectChars(mistake).charAt(0);
        char writtenCharsFirstChar = getWrittenChars(mistake).charAt(0);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        char nextChar = getNextChar(mistake);

        if ((writtenCharsFirstChar == 'b' && correctCharsFirstChar == 'p')
                || (writtenCharsFirstChar == 'p' && correctCharsFirstChar == 'b')
                || (writtenCharsFirstChar == 'd' && correctCharsFirstChar == 't')
                || (writtenCharsFirstChar == 't'  && correctCharsFirstChar == 'd')
                || (writtenCharsFirstChar == 'ď'  && correctCharsFirstChar == 'ť')
                || (writtenCharsFirstChar == 'ť'  && correctCharsFirstChar == 'ď')
                || (writtenCharsFirstChar == 'g'  && correctCharsFirstChar == 'k')
                || (writtenCharsFirstChar == 'k'  && correctCharsFirstChar == 'g')
                || (writtenCharsFirstChar == 'v'  && correctCharsFirstChar == 'f')
                || (writtenCharsFirstChar == 'f'  && correctCharsFirstChar == 'v')
                || (writtenCharsFirstChar == 'ž'  && correctCharsFirstChar == 'š')
                || (writtenCharsFirstChar == 'š'  && correctCharsFirstChar == 'ž')
                || (writtenCharsFirstChar == 'c'  && correctCharsFirstChar == 'h')
                || (writtenCharsFirstChar == 'h'  && correctCharsFirstChar == 'c')
                ) {

            if (((mistakeCharPosInWord == getCorrectWordLength(mistake))
                    || (mistakeCharPosInWord == getCorrectWordLength(mistake) - 1)
                    && ((correctWord.endsWith(".")) || ((correctWord.endsWith(".")) || ((correctWord.endsWith("."))))))
                    || (((writtenCharsFirstChar == 'c') && (correctCharsFirstChar == 'h')))
                    && ((mistakeCharPosInWord == (correctWord.length() - 1)) && nextChar == 'h')) {

                return "Souhlásky rozdělujeme na znělé (b, d, ď, g, h, v, z, ž) a neznělé (p, t, ť, k, ch, f, s, š). " +
                        "Na konci slov je každá souhláska vyslovována nezněle. Pomůcka: Říci si slovo v jiném tvaru. " +
                        "Např. potah zní jako [potach], jiný tvar: bez potahu. (VOJ)";

            } else if (mistakeCharPosInWord != correctWord.length()) {

                return "Skupina párových souhlásek se vyslovuje celá buď zněle, nebo nezněle, " +
                        "ale označování jednotlivých hlásek se řídí podle toho, které hlásky jsou " +
                        "v jiných tvarech téhož slova nebo ve slovech příbuzných, " +
                        "např. vézt (vezu), kavka (kavek), sjezd (sjezdu), lehký (lehounký). (PČP)";
            }
        }
        return null;
    }

    private String diakritikaOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String writtenWord = mistake.getWrittenWord();
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);
        char nextChar = getNextChar(mistake);

        Set<String> predpony = CorrectorRulesStaticLists.getPredponySlovesneAOstatni();

        if ((writtenChars.equals("u") && correctChars.equals("ú"))
                || (writtenChars.equals("ů") && correctChars.equals("ú"))) {

            if (mistakeCharPosInWord == 1) {
                return "Na začátku slova se dlouhé u vždy vyjadřuje jako ú. (VOJ)";

            } else if ((mistakeCharPosInWord > 1) && ((lemma.endsWith("úhelník"))
                    || (((prefixMatchesSet(lemma, predpony)) != null)
                    && (writtenWord.charAt((prefixMatchesSet(lemma, predpony)).length() + 1) == 'ú')))) {

                return "Slovo složené z více kořenů (např. troj + úhelník) si ú uchovává i uvnitř slova. (VOJ)";
            } else {

                return "Jde o přejaté slovo (ocún, múza), které se takto odlišuje od slov domácích (kůlna, důl, …). " +
                        "Ú se vyskytuje také u citoslovcí (hú, vrkú). (VOJ)";
            }
        } else if ((writtenChars.equals("u") && correctChars.equals("ů"))
                || (writtenChars.equals("ú") && correctChars.equals("ů"))) {

            return "U nepřejatých slov (např. kůlna, důl) je dlouhé u, které není na začátku slova, vyjádřeno jako ů.";
        } else if (((writtenChars.equals("ď") && correctChars.equals("d"))
                || (writtenChars.equals("ť") && correctChars.equals("t"))
                || (writtenChars.equals("ň") && correctChars.equals("n")))
                && ((nextChar == 'i') || (nextChar == 'í') || (nextChar == 'ě'))) {

            return "Háček (tzv. klička) zaniká, i přes odlišnou výslovnost. " +
                    "Měkkost přechází na samohlásku e (stane se z ní ě, např.: dělat) nebo i (např.: dílna). (VOJ)";

        } else if ((writtenChars.equals("d") && correctChars.equals("ď"))
                || (writtenChars.equals("t") && correctChars.equals("ť"))
                || (writtenChars.equals("n") && correctChars.equals("ň"))) {

            return "U ď, ť, ň na konci slova dochází k uchování háčku (např.: loď, choť). " +
                    "Stejně tak, pokud následují samohlásky a, o, u. (např.: ďábel) (VOJ)";
        }
        return null;

    }

    private String iPoMekkychaObojetnychSouhlaskachOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        String lemma = getLemma(mistake);
        String tag = getTag(mistake);
        char precedingChar = getPrecedingChar(mistake);

        if (mistakeCharPosInWord < 1) return null;

        List<Character> souhlaskyMekkeObojetne = CorrectorRulesStaticLists.getSouhlaskyMekkeAObojetne();
        List<Character> souhlaskyMekke = CorrectorRulesStaticLists.getSouhlaskyMekke();
        List<Character> souhlaskyObojetne = CorrectorRulesStaticLists.getSouhlaskyObojetne();
        List<Character> souhlaskyTvrde = CorrectorRulesStaticLists.getSouhlaskyTvrde();

        if ((writtenChars.equals("ý") && correctChars.equals("í"))
                || (writtenChars.equals("y") && correctChars.equals("i"))
                && (souhlaskyMekkeObojetne.contains(precedingChar))
                && (lemma.charAt(mistakeCharPosInWord - 2) == precedingChar)
                && (lemma.charAt(mistakeCharPosInWord - 1) == 'i') || (lemma.charAt(mistakeCharPosInWord - 1) == 'í')
                && ((!tag.startsWith("k2")) || (tag.startsWith("k2") && mistakeCharPosInWord != correctWord.length()))) {

            if (souhlaskyObojetne.contains(precedingChar)) {
                return "Tato slova mají společné psaní i/í po souhláskách (b, f, m, l, p, s, v, z), " +
                        "které jsou tzv. obojetné, a ke každému ze zmíněných písmen existuje seznam výjimek " +
                        "(tzv. vyjmenovaná slova), u nichž se na místo i/í píše y/ý (např.: východ, pýcha, bylina). " +
                        "Y/ý se píše také po některých přejatých slovech. (Např.: fyziognomie, zygota). (VOJ)";

            } else if (souhlaskyMekke.contains(precedingChar)) {
                return "Patří mezi pravopisně měkké souhlásky (ž, š, č, ř, c, j, ď, ť, ň), " +
                        "které jsou vždy následovány i/í.";
            }

        } else if ((writtenChars.equals("í") && correctChars.equals("ý"))
                || (writtenChars.equals("i") && correctChars.equals("y"))
                && (souhlaskyMekkeObojetne.contains(precedingChar))
                && (lemma.charAt(mistakeCharPosInWord - 2) == precedingChar)
                && (lemma.charAt(mistakeCharPosInWord - 1) == 'y') || (lemma.charAt(mistakeCharPosInWord - 1) == 'ý')
                && ((!tag.startsWith("k2")) || (tag.startsWith("k2") && mistakeCharPosInWord != correctWord.length()))
                && (souhlaskyTvrde.contains(precedingChar))) {
            return "Patří mezi pravopisně tvrdé souhlásky (h ch k r d t n), " +
                    "které jsou v domácích slovech následovány y/ý. (VOJ)";
        }
        return null;
    }

    private String zajmenaMneMeASlovaJeObsahujiciOrNull(Mistake mistake) {

        String correctChars = getCorrectChars(mistake);
        String writtenChars = getWrittenChars(mistake);
        String correctWord = getCorrectWord(mistake);
        char precedingChar = getPrecedingChar(mistake);
        char nextChar = getNextChar(mistake);

        if ((correctChars.equals("ně") && writtenChars.equals("ě"))
                || (correctChars.equals("ě") && writtenChars.equals("ně"))
                && (precedingChar == 'm') && (nextChar == 'ě')) {
            if (correctWord.length() <= 3) {
                return "Tvar mě se používá ve 2. a 4. pádu (Na mě nezbylo nic). " +
                        "Tvar mně ve 3. a 6. pádu. (Mně se líbí Ivana. -> Komu, čemu?, 3. pád.) " +
                        "Pomůcka: Zaměnit mě/mně za tebe/tobě. Tebe nahrazuje mě (Na tebe nezbylo nic.), " +
                        "tobě nahrazuje mně (Tobě se líbí Ivana.). (VOJ)";

            } else {
                return "Souhláska n po souhlásce m označuje výslovnost [ň + e]; " +
                        "např. měď, měkký. Tam, kde je souhláska [ň] nebo [n] i v jiném tvaru slova " +
                        "nebo v slově příbuzném, píše se mně; např. zapomněl (pomni), rozumně (rozumný).";
            }
        }
        return null;
    }

    //---------------- auxiliary methods -------------------------------------

    private String prefixMatchesSet(String lemmaWithoutAccentsOnIY, Set<String> set) {

        for (String key : set) {
            if (lemmaWithoutAccentsOnIY.startsWith(key)) {
                return key;
            }
        }

        return null;
    }

    private String getCorrectChars(Mistake mistake) {
        return mistake.getCorrectChars().toLowerCase();
    }

    private String getWrittenChars(Mistake mistake) {
        return mistake.getWrittenChars().toLowerCase();
    }

    private String getCorrectWord(Mistake mistake) {
        return mistake.getCorrectWord().toLowerCase();
    }

    private String getWrittenWord(Mistake mistake) {
        return mistake.getWrittenWord().toLowerCase();
    }

    private String getLemmaWithoutAccentsOnIY(Mistake mistake) {
        String lemma = mistake.getLemma();
        return lemma.replaceAll("ý", "ý").replaceAll("í", "i");
    }

    private String getLemma(Mistake mistake) {
        return mistake.getLemma();
    }

    private String getTag(Mistake mistake) {
        return mistake.getPosTag();
    }

    private Integer getMistakeCharPositionInWord(Mistake mistake) {
        if (mistake.getMistakeCharPosInWord() < 0) {
            return mistake.getMistakeCharPosInWord() * (-1);
        } else {
            return mistake.getMistakeCharPosInWord();
        }
    }

    private int getCorrectWordLength(Mistake mistake) {
        String correctWordWithoutInterpunction = getCorrectWord(mistake)
                .replace(".", "").replace("!", "").replace("?", "").replace("?", "");

        return correctWordWithoutInterpunction.length();
    }


    private Integer getWordPosition(Mistake mistake) {
        return mistake.getWordPosition();
    }

    private String getPreviousWord(Mistake mistake) {
        return mistake.getPreviousWord().toLowerCase().replace(".", "").replace("!", "").replace("?", "").replace(",", "");
    }

    private String getNextWord(Mistake mistake) {
        return mistake.getNextWord().toLowerCase().replace(".", "").replace("!","").replace("?","").replace(",", "");
    }

    private char getNextChar(Mistake mistake) {
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        if (mistakeCharPosInWord > (getCorrectWordLength(mistake) - 1)
                || mistakeCharPosInWord == 0) {
            return '#';
        } else {
            return getCorrectWord(mistake).charAt(mistakeCharPosInWord);
        }
    }

    private char getPrecedingChar(Mistake mistake) {
        Integer mistakeCharPosInWord = getMistakeCharPositionInWord(mistake);
        if (mistakeCharPosInWord < 2) {
            return '#';
        } else {
            return getWrittenWord(mistake).charAt(mistakeCharPosInWord - 2);
        }
    }

    private char getCorrectChar(Mistake mistake) {
        return 0;
    }
}
