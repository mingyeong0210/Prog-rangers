import React, { useState, useEffect } from 'react';
import { css } from "@emotion/react";
import { theme } from '../../components/Header/theme';
import { ojNameTag, tags } from "../Question/tagsform";
import axios from "axios";

export const CommentForm = () => {
    const [data, setData] = useState({ contents: [] });

    useEffect(() => {
        const token = "eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJJZCI6MSwiZXhwIjoxNjk2MjAwOTk1LCJpYXQiOjE2OTYxNjQ5OTUsImlzcyI6IlByb2dyYW5nZXJzIn0.I9b-_qt3ktwY-0d-2XGEq-vEpzxy8DdsGCvJzuf6TE40tUfO2ob94s5qB7L_G2qsDXN7pSbnZNb8z3tRz5GUsQ";
        const apiUrl = 'http://13.124.131.171:8080/api/v1/mypage/comments';

        axios.get(apiUrl, {
            method: "GET",
            headers: {Authorization: `Bearer ${token}`},
          })
          .then((response) => {
            setData(response.data);
          })
          .catch((error) => {
            console.error('API 요청 오류:', error);
          });
      }, []);

return(
    <>
    {data.contents.map((item, index) => (
        <div key={index} 
            css={css`display: inline-block; width: 100%; height: 220px; background-color: #F0F0F0;`}>
        
        <div css={css`max-width:800px; display: flex; align-items: center; justify-content: space-between;`}>           
            <div css={css`display: flex; align-items: center;`}>
            <img
                src= {item.solution.authorImageUrl || 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png'}                 
                alt='profileImg'
                width='50px'
                css={css`
                border-radius: 50%;
                margin : 20px 10px 10px 20px;
                `}></img>
            <div css={css`font-size:20px`}>{item.solution.authorNickname}</div>
            </div>
            <div css={css`
              ${ojNameTag}
              margin: 20px;
              background-color: ${item.solution.ojName === "프로그래머스" ? "#6AB4AC" : "#3578BF"};
              color: white;
            `}> 
              {item.solution.ojName}
            </div>
        </div>

          <div css={css`font-size: 22px; font-weight: 700; margin: 5px 0 10px 30px;`}>
            {item.solution.problemTitle}
          </div>

          <div css={css`font-size: 16px; margin: 5px 0 13px 30px;`}>
            {item.solution.solutionTitle}
          </div>

          <div css={css`
            width: 100%; 
            height: 39px; 
            margin-top: 10px;
            display: flex;
            flex-direction: row;
            justify-content: space-between;
          `}>
            <div css={css`display: flex; flex-direction: row; margin-left:30px;`}>
            <div key={item.solution.algorithm} css={css`${tags} background-color: #D9D9D9;`}>{item.solution.algorithm}</div>
            <div key={item.solution.dataStructure} css={css`${tags} background-color: #D9D9D9;`}>{item.solution.dataStructure}</div>
            </div>
          </div>
        </div>
    ))}

    {data.contents.map((item, index) => (
        <div key={index} 
        css={css`display:flex; align-items: center; width: 100%; height: 130px; border-bottom: 1px solid #DEDCDC;`}>
              
            <div css={css`display: flex; align-items: center; padding: 20px;`}>
            <img
                src= {item.comment.authorImageUrl || 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png'}                 
                alt='profileImg'
                width='80px'
                css={css`
                border-radius: 50%;
                `}></img>
                <div css={css`display:flex; flex-direction: column; margin-left:20px;`}>
                <div css={css`font-size:14px; margin-bottom:10px;`}>{item.comment.authorNickname}</div>
                <div css={css`font-size:20px`}>{item.comment.content}</div>
                </div>
            </div>

        </div>
    ))}
    </>
)
}