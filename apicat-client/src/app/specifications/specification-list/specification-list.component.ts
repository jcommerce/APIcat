import {Component, OnInit} from "@angular/core";
import {
  ModalResult,
  ConfirmationModalComponent,
  ModalAction
} from "../../shared/confirmation-modal/confirmation-modal.component";
import {AlertMessageService} from "../../shared/alert/alert-message.service";
import {LoadingIndicatorService} from "../../shared/loading-indicator/loading-indicator.service";
import {SpecificationService} from "../specification.service";
import {Specification} from "../../model/specification";

@Component({
  selector: 'specification-list',
  templateUrl: './specification-list.component.html',
  styleUrls: ['./specification-list.component.scss'],

})
export class SpecificationListComponent implements OnInit {
  specifications: Specification[];
  confirmationModal: ConfirmationModalComponent;

  constructor(private specificationService: SpecificationService,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  ngOnInit(): void {
    this.getSpecifications();
  }

  getSpecifications(): void {
    this.loadingService.showSpinner();
    this.specificationService.getAll()
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        specifications => this.specifications = specifications,
        error => this.alertMessageService.showErrorMessage("Unable to fetch specifications. Error:" + error));
  }

  delete(specification: Specification): void {
    this.confirmationModal.show(specification);
  }

  onModalClosed(result: ModalResult): void {
    if (result.action === ModalAction.POSITIVE) {
      let specification = result.topic as Specification;
      this.deleteSpecification(specification);
    }
  }

  private deleteSpecification(specification: Specification): void {
    this.loadingService.showSpinner();

    this.specificationService
      .delete(specification.id)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        () => this.specifications = this.specifications.filter(s => s !== specification),
        error => this.alertMessageService.showErrorMessage("Unable to delete specification. Error:" + error)
      );
  }

  onModalLoaded(modal: ConfirmationModalComponent): void {
    this.confirmationModal = modal;
  }

}
