import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';

import { FcLike, FcLikePlaceholder } from 'react-icons/fc';
import { RiShareBoxLine } from 'react-icons/ri';
import { flexLayout, indiLayout } from './indicatorSytle';
import { css } from '@emotion/react';
import { theme } from '../Header/theme';

export const Indicators = () => {

  const { solutionId } = useParams();
  const [ solution, setSolution ] = useState({});
  
  const [like, setLike] = useState(0);
  const [isLiked, setIsLiked] = useState(false);

  // const [scrapData, setScrapData] = useState({});

  useEffect(() => {
    const apiUrl = `http://13.124.131.171:8080/api/v1/solutions/${solutionId}`;

    axios
    .get(apiUrl)
    .then((response) => {
      setSolution(response.data.solution);
      setLike(response.data.solution.likes);
      setIsLiked(response.data.solution.pushedLike);
    })
    .catch((error) => {
      console.error('API 요청 오류:', error);
    });
  }, [solutionId]);
  
  // 좋아요 버튼
  const handleLikeClick = () => {

  const token = localStorage.getItem('token');
  const method = isLiked ? 'delete' : 'post';
  axios({
    method: method, // POST 또는 DELETE를 선택
    headers: {Authorization: `Bearer ${token}`},
    url: `http://13.124.131.171:8080/api/v1/solutions/${solutionId}/likes`
  })
  .then(()=> {
      setLike((prevLike) => (isLiked ? prevLike - 1 : prevLike + 1));
      setIsLiked(!isLiked);
    })
    .catch(error => {
      console.error(`좋아요 ${method} 실패`, error);
    });
  };

  // // 스크랩 버튼
  // const handleScrape = () => {
  //   const token = localStorage.getItem('token');

  //   if (!token) {
  //       alert("풀이를 스크랩하려면 로그인이 필요합니다.");
  //   } else {
  //       axios
  //           .post('http://13.124.131.171:8080/api/v1/mypage/solutions', scrapDataToSend, {
  //               method: "POST",
  //               headers: { Authorization: `Bearer ${token}` }
  //           })
  //           .then(response => {
  //             console.log('스크랩 성공', response.data);
  //           })
  //           .catch(error => {
  //             console.error('스크랩 실패:', error);
  //           });
  //       }
  //     }
  
  return (
    <>
    <div className="indicatorWrap" 
      css={css`${indiLayout} 
         border-bottom: 1px solid ${theme.colors.light3};
         color: ${theme.colors.dark2};`}>

      <div className="allIndicators" css={css`${flexLayout}`}>

        <div className="like" css={flexLayout}>
          <button onClick={handleLikeClick} className="icon" css={css`padding-right: 20px;`}>
            {isLiked ? <FcLike size="25" /> : <FcLikePlaceholder size="25" />}
          </button>
          <div
            css={css`
              padding-right: 20px;
            `}
          >
            {like}개
          </div>
        </div>
        <div className="scrap" css={flexLayout}>
          <button className="icon" css={css`padding-right: 20px;`}>
            <RiShareBoxLine size="25" color="#3486A0" />
          </button>
          <div>
            {solution.scraps}회
          </div>
        </div>
      </div>
    </div>
    </>
  );
};
