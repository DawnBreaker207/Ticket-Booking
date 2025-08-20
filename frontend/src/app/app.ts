import { Component } from "@angular/core";
import { Seat } from "./modules/home/components/layout/seat/seat";

@Component({
  selector: "app-root",
  imports: [Seat],
  templateUrl: "./app.html",
  styleUrl: "./app.css",
})
export class App {}
