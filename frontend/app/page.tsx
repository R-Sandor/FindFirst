"use client";
import "./main.css";
import TagList from "@components/TagList";
import { TagCntProvider } from "contexts/TagContext";
import BookmarkGroup from "@/components/bookmark/BookmarkGroup";
import { BookmarkProvider } from "@/contexts/BookmarkContext";
import UseAuth from "@components/UseAuth";
import { Badge } from "react-bootstrap";

export interface CardData {
  title: string;
  pdf: string;
  imgSrc: string;
  figure: string;
}

export default function App() {
  const userAuth = UseAuth();
  const catagories = [
    "Algorithms",
    "Architecture/Pipeline diagrams",
    "Bar charts",
    "Box Plots",
    "Confusion Matrix",
    "Graph",
    "Line Chart",
    "Maps",
    "Natural Images",
    "Neural Networks",
    "NLP rules/grammar",
    "Pie chart",
    "Scatter Plot",
    "Screenshots",
    "Tables",
    "Trees",
    "Pareto chart",
    "Venn Diagram",
    "Word Cloud",
  ];

  let cardData: CardData[] = [
    {
      title:
        "Table 2: The correlation between the self-reported Likert scale ratings...",
      pdf: "2020.acl-main.126.pdf",
      imgSrc: "2020.acl-main.126.pdf-Table2.png",
      figure: "Table"
    },
    {
      title: "Figure 2: Real-Time Hand Trajectory Tracking for the Sign FARM",
      pdf: "2020.signlang-1.22.pdf",
      imgSrc: "2020.signlang-1.22.pdf-Figure2.png",
      figure: "Neural Networks"
    },
    {
      title: "Table 1: Inter-agreement among human annotators",
      pdf: "C16-1101.pdf",
      imgSrc: "C16-1101.pdf-Table1.png",
      figure: "Table"
    },
    {
      title: "Figure 5: Dependency parsing: Confidence vs. unlabeled dependency accuracy",
      pdf: "D12-1091.pdf",
      imgSrc: "D12-1091.pdf-Figure5.png",
      figure: "Graph"
    },
    {
      title: "Table 1: Comparison of the me thods together with other...",
      pdf: "D16-1245.pdf",
      imgSrc: "D16-1245.pdf-Table1.png",
      figure: "Graph"
    },
    {
      title: "Table 9: The results show only the evidence class and are macro-averaged...",
      pdf: "D19-66.pdf",
      imgSrc: "D19-66.pdf-Table9.png",
      figure: "Table"
    },
  ];
  /**
   * Ideally when the user visits the site they will actually have a cool landing page
   * rather than redirecting them immediately to sign in.
   * Meaning that the '/' will eventually be added to the public route and not authenticated will be the
   * the regular landing.
   */
  // return userAuth ? (
  //   <BookmarkProvider>
  //     <TagCntProvider>
  //       <div className="row">
  //         <div className="col-md-4 col-lg-3">
  //           <TagList />
  //         </div>
  //         <div className="col-md-8 col-lg-9">
  //           <BookmarkGroup />
  //         </div>
  //       </div>
  //     </TagCntProvider>
  //   </BookmarkProvider>
  // ) : (
  //   <div> Hello Welcome to BookmarkIt. </div>
  // );

  return (
    <div className="row">
      <div className="row">
        <div className="pt-5 pb-5 half-width-form-control">
          <div className="input-group">
            <input
              type="search"
              className="form-control rounded"
              placeholder="Describe your figure!"
              aria-label="Search"
              aria-describedby="search-addon"
            />
            <button type="button" className="btn btn-outline-primary">
              search
            </button>
          </div>
        </div>
      </div>
      <div className="col-2">
        <div className="ml-6 features">
          Figure Types:
          {catagories.map((val, i) => {
            return (
              <div key={i} className="form-check">
                <input
                  className="ml-3 form-check-input"
                  type="checkbox"
                  value=""
                  id="flexCheckChecked"
                />
                <label className="form-check-label" htmlFor="flexCheckChecked">
                  {val}
                </label>
              </div>
            );
          })}
        </div>
      </div>
      <div className="col-9">
        <div className="row">
          {cardData.map((card, i) => {
            return (
              <div key={i} className="card mr-10 cstyle">
                <img
                  className="card-img"
                  src={card.imgSrc}
                  alt="Card image cap"
                />
                <div className="card-body">
                  <h5 className="card-title">{card.title}</h5>
                  <p className="card-text">{card.pdf}</p>
                  {/* <a href="#" className="btn btn-primary">
                    Go somewhere
                  </a> */}
                </div>
                  <div className="card-footer text-muted">
                    <Badge> {card.figure}</Badge>
                  </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
