package com.banking.bank;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
Main bank service impl.
 */
@Service
public class BankServiceImpl implements BankService
{
    private static final String BRAND = "Brand";
    private static final String BRANCH = "Branch";
    private static final String DATA = "data";

    @Autowired
    private Environment env;

    @Override
    public List<Branch> getBranches()
    {
        String bankUrl = env.getProperty("bank.url");
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity=new HttpEntity<String>(headers);
        ResponseEntity<String> response=restTemplate.exchange(bankUrl, HttpMethod.GET, entity,String.class);

        //check for ok status?
        //if (response.getStatusCode()==HttpStatus.OK)

        return parseJson(response.getBody());
    }

    @Override
    public List<Branch> getBranchesByCity(String city)
    {
        //get all branches and filter afterwards on city
        List<Branch> branches=getBranches();
        List<Branch> newBranches=new ArrayList();

        for (Branch branch:branches)
        {
            //city could be null
            if (city.equals(branch.getCity()))
            {
                newBranches.add(branch);
            }
        }
        return newBranches;
    }

    /*
    main parsing routine to parse json from api and return a list of Branches
     */
    private List<Branch> parseJson(String json)
    {
        List<Branch> list = new ArrayList<Branch>();
        JSONObject obj = new JSONObject(json);
        JSONArray arrayData = obj.getJSONArray(DATA);

        for (Object oData:arrayData)
        {
            JSONArray arrayBrand =  ((JSONObject)oData).getJSONArray(BRAND);

            for (Object oBrand:arrayBrand)
            {
                JSONArray arrayBranch = ((JSONObject) oBrand).getJSONArray(BRANCH);

                for (Object oBranch:arrayBranch)
                {
                    JSONObject branch = (JSONObject) oBranch;

                    Branch newBranch = new Branch();
                    newBranch.setBranchName(getJSONAttribute(branch, "Name"));
                    JSONObject postalObject=branch.getJSONObject("PostalAddress");
                    newBranch.setStreetAddress(getJSONArrayAttribute(postalObject, "AddressLine"));
                    newBranch.setCity(getJSONAttribute(postalObject, "TownName"));
                    newBranch.setCountrySubDivision(getJSONArrayAttribute(postalObject, "CountrySubDivision"));
                    newBranch.setCountry(getJSONAttribute(postalObject, "Country"));
                    newBranch.setPostCode(getJSONAttribute(postalObject, "PostCode"));

                    if (postalObject.has("GeoLocation")) {
                        JSONObject geoObject = postalObject.getJSONObject("GeoLocation");

                        if (geoObject.has("GeographicCoordinates")) {
                            JSONObject geoCoord = geoObject.getJSONObject("GeographicCoordinates");
                            newBranch.setLatitude(Float.valueOf(getJSONAttribute(geoCoord, "Latitude")));
                            newBranch.setLongitude(Float.valueOf(getJSONAttribute(geoCoord, "Longitude")));
                        }
                    }

                    list.add(newBranch);
                }
            }
        }

        return list;
    }

    private String getJSONArrayAttribute(JSONObject jsonObject, String jsonAttribute)
    {
        String ret = "";

        if (jsonObject.has(jsonAttribute))
        {
            JSONArray jsonArray = jsonObject.getJSONArray(jsonAttribute);

            for (Object oElement : jsonArray) {
                ret += (String) oElement;
            }
        }
        return ret;
    }

    private String getJSONAttribute(JSONObject jsonObject, String jsonAttribute)
    {
        if (jsonObject.has(jsonAttribute))
        {
            return jsonObject.getString(jsonAttribute);
        }
        return null;
    }
}