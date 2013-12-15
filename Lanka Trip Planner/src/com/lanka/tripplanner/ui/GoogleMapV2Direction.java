package com.lanka.tripplanner.ui;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.android.gms.maps.model.LatLng;
import android.util.Log;

public class GoogleMapV2Direction {
	
	public final static String MODE_OF_DRIVING = "driving";
	public final static String MODE_OF_WALKING = "walking";

	public GoogleMapV2Direction() {
	}

	// used-ok
	public Document getDocument(LatLng start, LatLng end, String mode) {
		String url = "http://maps.googleapis.com/maps/api/directions/xml?" + "origin="
				+ start.latitude + "," + start.longitude + "&destination=" + end.latitude + ","
				+ end.longitude + "&sensor=false&units=metric&mode=driving";
		Log.d("url", url);
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			InputStream in = response.getEntity().getContent();
			// DocumentBuilderFactory-Defines a factory API that enables applications to obtain a parser that produces
			// DOM object trees from XML documents
			// DocumentBuilder-Defines the API to obtain DOM Document instances from an XML document. Using this class,
			// an application can obtain a Document from XML.
			// DOM presents an XML document as a tree-structure->http://www.w3schools.com/dom/
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ok
	public String getDurationText(Document doc) {
		try {

			NodeList nl1 = doc.getElementsByTagName("duration");
			Node node1 = nl1.item(0);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "text"));
			Log.i("DurationText", node2.getTextContent());
			return node2.getTextContent();
		} catch (Exception e) {
			return "0";
		}
	}

	// ok
	public int getDurationValue(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("duration");
			Node node1 = nl1.item(0);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.i("DurationValue", node2.getTextContent());
			return Integer.parseInt(node2.getTextContent());
		} catch (Exception e) {
			return -1;
		}
	}

	// ok
	public String getDistanceText(Document doc) {

		try {
			NodeList nl1;
			nl1 = doc.getElementsByTagName("distance");

			Node node1 = nl1.item(nl1.getLength() - 1);// nl1.item(0)
			NodeList nl2 = null;
			nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.d("DistanceText", node2.getTextContent());
			return node2.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

	}

	// ok
	public int getDistanceValue(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("distance");
			Node node1 = null;
			node1 = nl1.item(nl1.getLength() - 1);
			NodeList nl2 = node1.getChildNodes();
			Node node2 = nl2.item(getNodeIndex(nl2, "value"));
			Log.i("DistanceValue", node2.getTextContent());
			return Integer.parseInt(node2.getTextContent());
		} catch (Exception e) {
			return -1;
		}

	}

	// ok
	public String getStartAddress(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("start_address");
			Node node1 = nl1.item(0);
			Log.i("StartAddress", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

	}

	// ok
	public String getEndAddress(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("end_address");
			Node node1 = nl1.item(0);
			Log.i("StartAddress", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}
	}

	// ok
	public String getCopyRights(Document doc) {
		try {
			NodeList nl1 = doc.getElementsByTagName("copyrights");
			Node node1 = nl1.item(0);
			Log.i("CopyRights", node1.getTextContent());
			return node1.getTextContent();
		} catch (Exception e) {
			return "-1";
		}

	}

	// used
	public ArrayList<LatLng> getDirection(Document doc) {
		NodeList nl1, nl2, nl3;
		ArrayList<LatLng> listOfGeopoints = new ArrayList<LatLng>();// list of points(latitude, longitude)
		nl1 = doc.getElementsByTagName("step");
		if (nl1.getLength() > 0) {
			for (int i = 0; i < nl1.getLength(); i++) {
				Node node1 = nl1.item(i);// consider each step
				nl2 = node1.getChildNodes();// list of child nodes of each step

				// this is the start location
				// //////////////////start_location//////////////////////////////////
				Node locationNode = nl2.item(getNodeIndex(nl2, "start_location"));
				nl3 = locationNode.getChildNodes();// access latitude,longitude
				Node latNode = nl3.item(getNodeIndex(nl3, "lat"));
				double lat = Double.parseDouble(latNode.getTextContent());
				Node lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				double lng = Double.parseDouble(lngNode.getTextContent());
				listOfGeopoints.add(new LatLng(lat, lng));
				// ////////////////////////////////////////////////////////////

				// intermediate locations- to draw path
				// ////////////////////////polyline//////////////////////////
				locationNode = nl2.item(getNodeIndex(nl2, "polyline"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "points"));
				ArrayList<LatLng> arrPoly = decodePolyline(latNode.getTextContent()); // set of points
				for (int j = 0; j < arrPoly.size(); j++) {
					listOfGeopoints.add(new LatLng(arrPoly.get(j).latitude,
							arrPoly.get(j).longitude));
				}
				// ////////////////////////////////////////////////////////////

				// this is the destination
				// ////////////////////////////end location/////////////////////////
				locationNode = nl2.item(getNodeIndex(nl2, "end_location"));
				nl3 = locationNode.getChildNodes();
				latNode = nl3.item(getNodeIndex(nl3, "lat"));
				lat = Double.parseDouble(latNode.getTextContent());
				lngNode = nl3.item(getNodeIndex(nl3, "lng"));
				lng = Double.parseDouble(lngNode.getTextContent());
				listOfGeopoints.add(new LatLng(lat, lng));
				// ///////////////////////////////////////////////////////////////////
			}
		}

		return listOfGeopoints;
	}

	// used by getDirections Function -ok
	private int getNodeIndex(NodeList nl, String nodename) {
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i).getNodeName().equals(nodename))
				return i;
		}
		return -1;
	}

	// used by getDirections - network
	private ArrayList<LatLng> decodePolyline(String encoded) {
		ArrayList<LatLng> point = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			point.add(position);
		}
		return point;
	}
}
