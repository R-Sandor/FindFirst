// pages/api/check-image.ts
import { NextApiRequest, NextApiResponse } from "next";
import fs from "fs";
import path from "path";

const IMAGE_DIR = process.env.NEXT_PUBLIC_IMAGE_DIR;
const SERVED_IMAGE = process.env.NEXT_PUBLIC_SERVER_URL + "/api/screenshots/";
export default function handler(req: NextApiRequest, res: NextApiResponse) {
  const { screenshot } = req.query;

  const publicPath = path.join(
    process.cwd(),
    "/public/screenshots",
    screenshot as string,
  );

  if (fs.existsSync(publicPath) && screenshot && IMAGE_DIR) {
    console.log("This is the path", publicPath);
    fs.readFile(publicPath, (err, data) => {
      if (err) {
        console.log(err);
        res.status(500).json({ error: "Unable to read file" });
        return;
      }
      res.setHeader("Content-Type", "application/octet-stream");
      res.setHeader(
        "Content-Disposition",
        `attachment; filename=${screenshot}`,
      );
      res.status(200).send(data);
    });
  } else if (screenshot && IMAGE_DIR) {
    console.log("Not found sending from api");
    res.status(200).send(SERVED_IMAGE + screenshot);
  }
}
