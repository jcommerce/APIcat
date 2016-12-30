import {Component} from '@angular/core';
import {Definition} from '../../model/definition';
import {DefinitionService} from '../definition.service';
import {Router} from '@angular/router';
import {AlertMessageService} from '../../shared/alert/alert-message.service';
import {LoadingIndicatorService} from '../../shared/loading-indicator/loading-indicator.service';

@Component({
  selector: 'app-definition-add',
  templateUrl: './definition-add.component.html',
  styleUrls: ['./definition-add.component.scss'],

})
export class DefinitionAddComponent {
  definition: Definition = new Definition();

  constructor(private definitionService: DefinitionService,
              private router: Router,
              private alertMessageService: AlertMessageService,
              private loadingService: LoadingIndicatorService) {
  }

  onSubmit(definition: Definition): void {
    this.loadingService.showSpinner();

    this.definitionService.create(definition)
      .finally(() => this.loadingService.hideSpinner())
      .subscribe(
        createdDefinition => this.router.navigate(['/definitions', createdDefinition.id]),
        error => this.alertMessageService.showErrorMessage('Unable to save definition. Error:' + error)
      );
  }
}
