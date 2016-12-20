import {Component} from "@angular/core";

@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  public title = 'Apicat';
  public isNavbarCollapsed: boolean = true;
}
