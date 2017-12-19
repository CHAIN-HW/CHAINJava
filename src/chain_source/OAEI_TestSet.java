package chain_source;

import java.util.ArrayList;
import java.util.StringJoiner;

public class OAEI_TestSet {
	
	double similarityThreshold = 0.0; // (0.3) Minimum similarity threshold to filter SPSM results (0.0 to 1.0)
	int maxMatchResults = 0 ; // (5) Maximum number of results to return - 0 if no limit
	
	
	private Call_SPSM spsm = new Call_SPSM();
	private Best_Match_Results filterRes = new Best_Match_Results();
	private Repair_Schema repairSchema = new Repair_Schema();
	
	private static String[] cmtSchemas = {
			"administrator(acceptPaper, addProgramCommitteeMember, assignReviewer, enableVirtualMeeting,"
			+" enterConferenceDetails, enterReviewCriteria, finalizePaperAssignment,"
			+" printHardcopyMailingManifests, rejectPaper, runPaperAssignmentTools, setMaxPapers,"
			+" startReviewerBidding)",
			"author(submitPaper, writePaper)",
			"bid(adjustedBy)",
			"coAuthor(coWritePaper)",
			"conference(acceptsHardcopySubmissions, date, detailsEnteredBy,"
			+" hardcopyMailingManifestsPrintedBy, hasConferenceMember, logoURL, name,"
			+" paperAssignmentFinalizedBy, paperAssignmentToolsRunBy, reviewCriteriaEnteredBy,"
			+" reviewerBiddingStartedBy, reviewsPerPaper, siteURL, virtualMeetingEnabledBy)", 
			 "conferenceMember(memberOfConference)",
			"externalReviewer(assignedByReviewer)",
			"paper(acceptedBy, assignedTo, hasAuthor, hasBid, hasCoAuthor, hasDecision, hasSubjectArea,"
			+" paperID, readByMetaReviewer, readByReviewer, rejectedBy, title)",
			"person(email, hasConflictOfInterest)",
			"programCommittee(hasProgramCommitteeMember)",
			"programCommitteeChair(endReview)",
			"programCommitteeMember(addedBy, maxPapers, memberOfProgramCommittee)",
			"review(writtenBy)",
			"reviewer(adjustBid,assignExternalReviewer,assignedByAdministrator,hasBeenAssigned,"
			+ "readPaper,writeReview)"
					} ;
	// private static String[] cocusSchemas ;
	private static String[] conferenceSchemas = {
			"abstract(isTheThPartOf)",
			"activeConferenceParticipant(givesPresentations)",
			"chair(wasACommitteeChairOf)",
			"coChair(wasACommitteCoChairOf)",
			"committee(hasACommitteeChair, hasACommitteeCoChair, hasMembers, wasACommitteeOf)",
			"committeeMember(wasAMemberOf)",
			"conferenceContribution(isSubmittedAt)",
			"conferenceDocument(hasADateOfIssue, hasAuthors)",
			"conferencePart(hasATrackWorkshopTutorialChair, hasATrackWorkshopTutorialTopic," 
			+ " isPartOfConferenceVolumes)",
			"conferenceProceedings(hasAPublisher, hasAVolume, hasAnISBN)",
			"conferenceVolume(hasACommtitee, hasALocation, hasAProgramCommittee, hasASteeringCommittee," + " hasAnOrganizingCommittee, hasContributions, hasImportantDates, hasParts, hasTracks,"
			+ " hasTutorials, hasWorkshops)",
			"conferenceWww(hasAURL)",
			"importantDates(belongToAConferenceVolume, isADateOfAcceptanceAnnouncement,"
			+ " isADateOfCameraReadyPaperSubmission, isAFullPaperSubmissionDate, isAStartingDate,"
			+ " isAnAbstractSubmissionDate, isAnEndingDate)",
			"organizingCommittee(wasAnOrganizingCommitteeOf)",
			"person(contributes, hasAnEmail, hasGender, hasTheFirstName, hasTheLastName)",
			"presentation(isGivenBy)",
			"programCommittee(wasAProgramCommitteeOf)",
			"publisher(issues)",
			"review(hasAuthors, reviews)" ,
			"reviewExpertise(belongsToReviewers,hasASubmittedContribution,hasAnExpertise)",
			"reviewPreference(belongsToReviewers, hasADegree, hasATopicOrASubmissionContribution)",
			"reviewedContribution(hasAReview)",
			"reviewer(hasAReviewReferenceOrExpertise, invitedBy, invitesCoReviewers)",
			"steeringCommittee(wasASteeringCommitteeOf)",
			"submittedContribution(hasAReviewExpertise)",
			"topic(belongsToAReviewReference, isATopisOfConferenceParts)",
			"trackWorkshopChair(wasATrackWorkshopChairOf)" 
			} ;
	// private static String[] confiousSchemas ;
	private static String[] confOfSchemas = {
		    "administrativeEvent(follows, parallelWith)",
		    "author(writes)",
		    "contribution(abstract, contactEmail, dealsWith, hasKeyword, hasTitle, remark, writtenBy)",
		    "event(endsOn, startsOn)",
		    "memberPC(expertOn, reviewes)",
		    "participant(earlyRegistration)",
		    "person(employedBy, hasCity, hasCountry, hasEmail, hasFax, hasFirstName, hasHomePage, hasPhone, hasPostalCode, hasStreet, hasSurname)",
		    "scholar(studyAt)",
		    "socialEvent(defaultChoice, hasTitle, location)",
		    "workingEvent(defaultChoice, hasAdministrativeEvent, hasTopic, hasTitle, location)"
	};
	// private static String[] crs_drSchemas ;
	private static String[] edasSchemas = {
			"academicEvent(hasCall)",
		    "acceptedPaper(relatedToEvent)",
		    "activePaper(hasRating)",
		    "author(hasRelatedPaper)",
		    "call(forEvent, hasSubmissionDeadline, hasSubmissionInstructions, isInitiatedBy)",
		    "conference(endDate, hasCountry, hasMember, hasName, hasTopic, manuscriptDueOn, paperDueOn, registrationDueOn, startDate)",
		    "conferenceEvent(hasAttendee, hasEndDateTime, hasLocation, hasProgramme, hasStartDateTime)",
		    "contactInformation(hasCity, hasPhone, hasPostalCode, hasStreet)",
		    "document(relatesTo)",
		    "mealEvent(hasMenu)",
		    "mealMenu(isMenuOf)",
		    "organization(isProviderOf)",
		    "paper(isReviewedBy, isWrittenBy)",
		    "paperPresentation(relatedToPaper)",
		    "person(attendeeAt, hasBiography, hasFirstName, hasLastName, isMemberOf)",
		    "personalReviewHistory(isReviewHistoryOf)",
		    "place(isLocationOf)",
		    "programme(belongsToEvent)",
		    "reviewer(hasReviewHistory)",
		    "sponsorship(hasCostAmount, hasCostCurrency, providedBy)",
		    "topic(isTopicOf)"
	};
	private static String[] ekawSchemas = {
			"academicInstitution(scientificallyOrganises)",
		    "acceptedPaper(hasReview)",
		    "assignedPaper(hasReviewer)",
		    "cameraReadyPaper(updatedVersionOf, writtenBy)",
		    "conferenceProceedings(volumeContainsPaper)",
		    "conferenceSession(partOfEvent)",
		    "contributedTalk(presentationOfPaper)",
		    "document(hasUpdatedVersion, updatedVersionOf, writtenBy)",
		    "evaluatedPaper(hasReview)",
		    "event(eventOnList, hasEvent, heldIn, organisedBy, partOfEvent)",
		    "individualPresentation(presentationOfPaper)",
		    "location(locationOf)",
		    "organisation(publisherOf, technicallyOrganises)",
		    "paper(hasReview, hasReviewer)",
		    "person(authorOf)",
		    "possibleReviewer(reviewerOfPaper)",
		    "proceedings(volumeContainsPaper)",
		    "researchTopic(topicCoveredBy)",
		    "review(reviewOfPaper, reviewWrittenBy)",
		    "session(hasEvent, partOfEvent)"
	};
	private static String[] iastedSchemas = {
			"activity(isDatedOn, isHeldAfter, isHeldBefore, isHeldIn)",
		    "activityAfterConference(isHeldAfter)",
		    "activityBeforeConference(isHeldBefore)",
		    "audiovisualEquipment(isDesignedFor, isSituatedIn, isUsedBy)",
		    "author(give, prepare)",
		    "authorAttendeeBookRegistrationFee(isPaidFor)",
		    "authorAttendeeCdRegistrationFee(isPaidFor)",
		    "authorBookProceedingsIncluded(obtain)",
		    "authorCdProceedingsIncluded(obtain)",
		    "authorInformationForm(isSentAfter)",
		    "briefIntroductionForSessionChair(isGivenBy, isGivenTo)",
		    "city(isSituatedIn)",
		    "coctailReception(isDesignedFor)",
		    "coffeeBreak(isDesignedFor)",
		    "conferenceActivity(isHeldIn)",
		    "conferenceAirport(isSituatedIn)",
		    "conferenceBuilding(isOccupiedBy, isSituatedIn)",
		    "conferenceCity(isVisitedBy)",
		    "conferenceDays(isDesignedFor)",
		    "conferenceHall(isSituatedIn)",
		    "conferenceHotel(isDesignedFor, isSituatedIn)",
		    "conferenceRestaurant(isOccupiedBy, isSituatedIn)",
		    "conferenceState(isVisitedBy)",
		    "creditCard(isDesignedFor)",
		    "deadline(doneTill, isDesignedFor)",
		    "delegate(need, obtain, occupy, pay, send)",
		    "departureTax(isDesignedFor)",
		    "dinnerBanquet(isDesignedFor)",
		    "fee(isPaidWith)",
		    "feeForExtraTrip(isPaidBy, isPaidFor)",
		    "finalManuscript(goThrough)",
		    "form(isGivenTo)",
		    "fullDayTour(isDesignedFor, isHeldIn)",
		    "hotelFee(isPaidBy)",
		    "hotelRegistrationForm(isSentBefore)",
		    "hotelRoom(isDesignedFor, isSituatedIn)",
		    "initialManuscipt(goThrough)",
		    "invitationLetter(isDesignedFor)",
		    "item(goThrough, isGivenTo, isMadeFrom, isNeededFor, isPreparedBy, isSentAfter, isSentBefore, isSentBy, isSignedBy, isUsedBy, isUsedFor, isWritenBy)",
		    "lecture(isDesignedFor)",
		    "lecturer(speakIn)",
		    "listener(isPresentIn, sign)",
		    "mailingList(isDesignedFor)",
		    "memeberRegistrationFee(isDesignedFor)",
		    "money(hasAmountOf, isPaidBy, isPaidFor, isPaidIn, isPaidWith)",
		    "nonauthorRegistrationFee(isDesignedFor)",
		    "nonmemberRegistrationFee(isDesignedFor)",
		    "paymentDocument(isDesignedFor)",
		    "person(give, isPresent, isPresentIn, need, obtain, occupy, pay, prepare, send, sign, speakIn, write)",
		    "place(isEquippedBy, isSituatedIn)",
		    "plenaryLectureSpeaker(speakIn)",
		    "presenterCity(isOccupiedBy)",
		    "presenterHouse(isOccupiedBy, isSituatedIn)",
		    "presenterState(isOccupiedBy)",
		    "presenterUniversity(isSituatedIn)",
		    "publication(isMadeFrom)",
		    "recordOfAttendance(isSignedBy, isSituatedIn)",
		    "registration(isDesignedFor)",
		    "registrationFee(isPaidBy, isPaidFor)",
		    "registrationForm(isSentBefore, isSentBy)",
		    "renting(isConnectedWith)",
		    "review(isWritenBy)",
		    "session(isGivenBy, isHeldIn)",
		    "sessionChair(obtain)",
		    "sessionRoom(isEquippedBy, isSituatedIn)",
		    "socialProgram(isDesignedFor)",
		    "speaker(give, write)",
		    "sponsor(occupy)",
		    "sponsorCity(isOccupiedBy)",
		    "sponsorCompanyHouse(isOccupiedBy, isSituatedIn)",
		    "studentRegistrationFee(isDesignedFor)",
		    "submission(isWritenBy)",
		    "technicActivity(isGivenBy)",
		    "technicalCommitee(goThrough)",
		    "tip(isPaidIn)",
		    "transparency(isPreparedBy)",
		    "transportVehicle(isUsedBy)",
		    "tripCity(isVisitedBy)",
		    "tripDay(isDesignedFor)",
		    "tutorialSpeaker(speakIn)",
		    "viza(isNeededFor)"
	};
	// private static String[] linklingsSchemas ;
	// private static String[] mICROSchemas ;
	// private static String[] myReviewSchemas ;
	// private static String[] openConfOfSchemas ;
	// private static String[] paperDyneSchemas ;
	// private static String[] pcssSchemas ;
	private static String[] sigkddSchemas = {
			"aCMSIGKDD(design, hold, search)",
		    "author(notificationUntil, obtain, submit)",
		    "authorOfPaper(award)",
		    "authorOfPaperStudent(award)",
		    "award(awardedBy)",
		    "conference(cityOfConference, endOfConference, nameOfConference, startOfConference, holdedBy)",
		    "deadline(date, designedBy)",
		    "document(presentationedBy, submitUntil)",
		    "person(eMail, name, nation, canStayIn, pay)",
		    "registrationFee(currency, price, payedBy)",
		    "speaker(presentation)",
		    "sponzor(nameOfSponsor, searchedBy)"
	};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OAEI_TestSet OAEITest = new OAEI_TestSet();
		
		
		StringJoiner targets = new StringJoiner(";");
		for(String target: sigkddSchemas) {
			targets.add(target) ;
		}
		String targetSchemas = targets.toString() ;
		
		System.out.println("All Target Schemas: " + targetSchemas);
		
		
		for(String sourceSchema: edasSchemas) {
			System.out.println("\n\nSource: " + sourceSchema);
			ArrayList<Match_Struc>  results = OAEITest.startRepair(sourceSchema, targetSchemas) ;
			if (results != null) {
				for(Match_Struc result: results) {
					System.out.println("Repair: " + result.getRepairedSchema()) ;
				}
			}
		}
		Call_SPSM.reportSPSM() ;
		
	}
	
public ArrayList<Match_Struc> startRepair(String sourceSchema, String targetSchemas)
{
		
		//Create an empty match structure
		Match_Struc current = new Match_Struc();
		// System.out.println(sourceSchema);
		current.setQuerySchema(sourceSchema);
		String [] head = sourceSchema.split("[,)(]");
		// System.out.println(head [0]);
		
		current.setQuerySchemaHead(head [0] );
		
			
		//Narrow down the target schemas by filtering them against the associated words in the source schema
		targetSchemas = Narrow_Down.narrowDown(current.getQuerySchemaHead(), targetSchemas) ;
		
		System.out.println("Narrowed Target Schemas: " + targetSchemas);
		
		//start off by calling SPSM with schema created from query
		//and target schemas passed in originally
		ArrayList<Match_Struc> results = new ArrayList<Match_Struc>();
		
		try {
			results = spsm.callSPSM(results, current.getQuerySchema(), targetSchemas);
		
			if(results == null){
				System.out.println("getSchemas returned null.");
			}else if(results.size()==0){
				//there are no results from spsm
				System.out.println("SPSM returned no matches.");
			}else{
				//spsm has returned something
				//filter results
				System.out.println("SPSM returned matches.");
			
				results = filterRes.getThresholdAndFilter(results, similarityThreshold, maxMatchResults);
			
				//return repaired schema
				results = repairSchema.repairSchemas(results);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return results;
	}

}
