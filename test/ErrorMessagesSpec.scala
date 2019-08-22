/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import assets.messages._
import base.GuiceAppSpecBase
import play.api.i18n.Messages

class ErrorMessagesSpec extends GuiceAppSpecBase {

  "Messages file" should {

    "for the Setup Section" should {

      "have the correct error messages for the WantToFindOutPage" in {
        Messages("whatDoYouWantToFindOut.error.required") mustBe WhatDoYouWantToFindOutMessages.error
      }

      "have the correct error messages for the WhoAreYouPage" in {
        Messages("whoAreYou.ir35.error.required") mustBe WhoAreYouMessages.ir35Error
        Messages("whoAreYou.paye.error.required") mustBe WhoAreYouMessages.payeError
      }

      "have the correct error messages for the WorkingUsingIntermediaryPage" in {
        Messages("worker.workerUsingIntermediary.error.required") mustBe WorkerUsingIntermediaryMessages.Worker.error
        Messages("hirer.workerUsingIntermediary.error.required") mustBe WorkerUsingIntermediaryMessages.Hirer.error
      }

      "have the correct error messages for the ContractStartedPage" in {
        Messages("worker.optimised.contractStarted.error.required") mustBe ContractStartedOptimisedMessages.Worker.error
        Messages("hirer.optimised.contractStarted.error.required") mustBe ContractStartedOptimisedMessages.Hirer.error
      }
    }


    "for the Early Exit Section" should {

      "have the correct error messages for the OfficeHolderPage" in {
        Messages("worker.optimised.officeHolder.error.required") mustBe OfficeHolderMessages.Optimised.Worker.error
        Messages("hirer.optimised.officeHolder.error.required") mustBe OfficeHolderMessages.Optimised.Hirer.error
      }
    }


    "for the Personal Service Section" should {

      "have the correct error messages for the ArrangedSubstitutePage" in {
        Messages("worker.optimised.arrangedSubstitute.error.required") mustBe ArrangedSubstituteMessages.Optimised.Worker.error
        Messages("hirer.optimised.arrangedSubstitute.error.required") mustBe ArrangedSubstituteMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the RejectSubstitutePage" in {
        Messages("worker.optimised.rejectSubstitute.error.required") mustBe RejectSubstituteMessages.Optimised.Worker.error
        Messages("hirer.optimised.rejectSubstitute.error.required") mustBe RejectSubstituteMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the DidPaySubstitutePage" in {
        Messages("worker.optimised.didPaySubstitute.error.required") mustBe DidPaySubstituteMessages.Optimised.Worker.error
        Messages("hirer.optimised.didPaySubstitute.error.required") mustBe DidPaySubstituteMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the WouldWorkerPaySubstitutePage" in {
        Messages("worker.optimised.wouldWorkerPaySubstitute.error.required") mustBe WouldPaySubstituteMessages.Optimised.Worker.error
        Messages("hirer.optimised.wouldWorkerPaySubstitute.error.required") mustBe WouldPaySubstituteMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the NeededToPayHelperPage" in {
        Messages("worker.optimised.neededToPayHelper.error.required") mustBe NeededToPayHelperMessages.Optimised.Worker.error
        Messages("hirer.optimised.neededToPayHelper.error.required") mustBe NeededToPayHelperMessages.Optimised.Hirer.error
      }
    }


    "for the Control Section" should {

      "have the correct error messages for the MoveWorkerPage" in {
        Messages("worker.optimised.moveWorker.error.required") mustBe MoveWorkerMessages.OptimisedWorker.error
        Messages("hirer.optimised.moveWorker.error.required") mustBe MoveWorkerMessages.OptimisedHirer.error
      }

      "have the correct error messages for the HowWorkIsDonePage" in {
        Messages("worker.optimised.howWorkIsDone.error.required") mustBe HowWorkIsDoneMessages.OptimisedWorker.error
        Messages("hirer.optimised.howWorkIsDone.error.required") mustBe HowWorkIsDoneMessages.OptimisedHirer.error
      }

      "have the correct error messages for the ScheduleOfWorkingHoursPage" in {
        Messages("worker.optimised.scheduleOfWorkingHours.error.required") mustBe ScheduleOfWorkingHoursMessages.OptimisedWorker.error
        Messages("hirer.optimised.scheduleOfWorkingHours.error.required") mustBe ScheduleOfWorkingHoursMessages.OptimisedHirer.error
      }

      "have the correct error messages for the ChooseWhereWorkPage" in {
        Messages("worker.optimised.chooseWhereWork.error.required") mustBe ChooseWhereWorkMessages.OptimisedWorker.error
        Messages("hirer.optimised.chooseWhereWork.error.required") mustBe ChooseWhereWorkMessages.OptimisedHirer.error
      }
    }


    "for the Financial Risk Section" should {

      "have the correct error messages for the EquipmentExpensesPage" in {
        Messages("worker.equipmentExpenses.error.required") mustBe EquipmentExpensesMessages.Worker.error
        Messages("hirer.equipmentExpenses.error.required") mustBe EquipmentExpensesMessages.Hirer.error
      }

      "have the correct error messages for the VehiclePage" in {
        Messages("worker.vehicle.error.required") mustBe VehicleMessages.Worker.error
        Messages("hirer.vehicle.error.required") mustBe VehicleMessages.Hirer.error
      }

      "have the correct error messages for the MaterialsPage" in {
        Messages("worker.materials.error.required") mustBe MaterialsMessages.Worker.error
        Messages("hirer.materials.error.required") mustBe MaterialsMessages.Hirer.error
      }

      "have the correct error messages for the OtherExpensesPage" in {
        Messages("worker.otherExpenses.error.required") mustBe OtherExpensesMessages.Worker.error
        Messages("hirer.otherExpenses.error.required") mustBe OtherExpensesMessages.Hirer.error
      }

      "have the correct error messages for the HowWorkerIsPaidPage" in {
        Messages("worker.optimised.howWorkerIsPaid.error.required") mustBe HowWorkerIsPaidMessages.WorkerOptimised.error
        Messages("hirer.optimised.howWorkerIsPaid.error.required") mustBe HowWorkerIsPaidMessages.HirerOptimised.error
      }

      "have the correct error messages for the PutRightAtOwnCostsPage" in {
        Messages("worker.optimised.putRightAtOwnCost.error.required") mustBe PutRightAtOwnCostsMessages.WorkerOptimised.error
        Messages("hirer.optimised.putRightAtOwnCost.error.required") mustBe PutRightAtOwnCostsMessages.HirerOptimised.error
      }
    }


    "for the Part and Parcel Section" should {

      "have the correct error messages for the BenefitsPage" in {
        Messages("worker.optimised.benefits.error.required") mustBe BenefitsMessages.Optimised.Worker.error
        Messages("hirer.optimised.benefits.error.required") mustBe BenefitsMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the LineManagerDutiesPage" in {
        Messages("worker.optimised.lineManagerDuties.error.required") mustBe LineManagerDutiesMessages.Optimised.Worker.error
        Messages("hirer.optimised.lineManagerDuties.error.required") mustBe LineManagerDutiesMessages.Optimised.Hirer.error
      }

      "have the correct error messages for the IdentifyToStakeholdersPage" in {
        Messages("worker.optimised.identifyToStakeholders.error.required") mustBe IdentifyToStakeholdersMessages.Optimised.Worker.error
        Messages("hirer.optimised.identifyToStakeholders.error.required") mustBe IdentifyToStakeholdersMessages.Optimised.Hirer.error
      }
    }


    "for the Business On Own Account Section" should {

      "have the correct error messages for the WorkerKnownPage" in {
        Messages("workerKnown.error.required") mustBe WorkerKnownMessages.Hirer.error
      }

      "have the correct error messages for the MultipleContractsPage" in {
        Messages("worker.multipleContracts.error.required") mustBe MultipleContractsMessages.Worker.error
        Messages("hirer.multipleContracts.error.required") mustBe MultipleContractsMessages.Hirer.error
      }

      "have the correct error messages for the PermissionToWorkWithOthersPage" in {
        Messages("worker.permissionToWorkWithOthers.error.required") mustBe PermissionToWorkWithOthersMessages.Worker.error
        Messages("hirer.permissionToWorkWithOthers.error.required") mustBe PermissionToWorkWithOthersMessages.Hirer.error
      }

      "have the correct error messages for the OwnershipRightsPage" in {
        Messages("ownershipRights.error.required") mustBe OwnershipRightsMessages.error
      }

      "have the correct error messages for the TransferOfRightsPage" in {
        Messages("worker.transferOfRights.error.required") mustBe TransferOfRightsMessages.Worker.error
        Messages("hirer.transferOfRights.error.required") mustBe TransferOfRightsMessages.Hirer.error
      }

      "have the correct error messages for the RightsOfWorkPage" in {
        Messages("worker.rightsOfWork.error.required") mustBe RightsOfWorkMessages.Worker.error
        Messages("hirer.rightsOfWork.error.required") mustBe RightsOfWorkMessages.Hirer.error
      }

      "have the correct error messages for the PreviousContractPage" in {
        Messages("worker.previousContract.error.required") mustBe PreviousContractMessages.Worker.error
        Messages("hirer.previousContract.error.required") mustBe PreviousContractMessages.Hirer.error
      }

      "have the correct error messages for the FollowOnContractPage" in {
        Messages("followOnContract.error.required") mustBe FollowOnContractMessages.error
      }

      "have the correct error messages for the FirstContractPage" in {
        Messages("worker.firstContract.error.required") mustBe FirstContractMessages.Worker.error
        Messages("hirer.firstContract.error.required") mustBe FirstContractMessages.Hirer.error
      }

      "have the correct error messages for the ExtendContractPage" in {
        Messages("extendContract.error.required") mustBe ExtendContractMessages.error
      }

      "have the correct error messages for the MajorityOfWorkingTimePage" in {
        Messages("worker.majorityOfWorkingTime.error.required") mustBe MajorityOfWorkingTimeMessages.Worker.error
        Messages("hirer.majorityOfWorkingTime.error.required") mustBe MajorityOfWorkingTimeMessages.Hirer.error
      }

      "have the correct error messages for the FinanciallyDependentPage" in {
        Messages("worker.financiallyDependent.error.required") mustBe FinanciallyDependentMessages.Worker.error
        Messages("hirer.financiallyDependent.error.required") mustBe FinanciallyDependentMessages.Hirer.error
      }

      "have the correct error messages for the SimilarWorkOtherClientsPage" in {
        Messages("worker.similarWorkOtherClients.error.required") mustBe SimilarWorkOtherClientsMessages.Worker.error
        Messages("hirer.similarWorkOtherClients.error.required") mustBe SimilarWorkOtherClientsMessages.Hirer.error
      }
    }
  }
}