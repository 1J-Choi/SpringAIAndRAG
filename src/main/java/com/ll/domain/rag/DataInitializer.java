package com.ll.domain.rag;

import com.ll.domain.post.entity.Post;
import com.ll.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final PostRepository postRepository;
    private final RagService ragService;

    @Bean
    CommandLineRunner initRagData() {
        return args -> {
            if (postRepository.count() == 0) {
                System.out.println("🚀 RAG 초기 데이터 로딩 시작...");

                List<Post> posts = List.of(
                        new Post(
                                "맥북 프로 16인치 M3 대여",
                                "전자기기",
                                "2023년 M3 Pro 칩셋, 36GB RAM. 영상 편집, 개발 작업에 최적화되어 있습니다.",
                                30000,
                                "서울 강남구",
                                "거의 새것",
                                null
                        ),

                        new Post(
                                "캐논 EOS R5 미러리스 카메라",
                                "카메라",
                                "4500만 화소 풀프레임 미러리스 카메라입니다. 결혼식, 행사 촬영용으로 적합합니다.",
                                50000,
                                "서울 마포구",
                                "양호",
                                "배터리 2개, 메모리카드 64GB, 카메라 가방"
                        ),

                        new Post(
                                "닌텐도 스위치 OLED 모델",
                                "게임기",
                                "젤다의 전설, 마리오 카트, 동물의 숲 등 인기 게임 5개 포함.",
                                20000,
                                "부산 해운대구",
                                "양호",
                                "프로컨트롤러 2개 포함"
                        ),

                        new Post(
                                "다이슨 V15 무선청소기",
                                "생활가전",
                                "최신형 무선청소기입니다. 흡입력 강하고 배터리 60분 지속됩니다.",
                                30000,
                                "서울 송파구",
                                "거의 새것",
                                "레이저 먼지 감지 기능, 다양한 브러시 헤드 포함"
                        ),

                        new Post(
                                "코베아 4인용 돔텐트",
                                "캠핑용품",
                                "4인용 돔텐트입니다. 방수 완벽하고 설치 쉽습니다.",
                                20000,
                                "경기 용인시",
                                "양호",
                                "그라운드시트, 팩, 수납가방"
                        ),

                        new Post(
                                "아이패드 프로 12.9인치 (2024)",
                                "전자기기",
                                "M4 칩 탑재, 애플펜슬 2세대 포함. 디자인 작업, 강의용으로 좋습니다. 저장공간: 256GB",
                                25000,
                                "서울 서초구",
                                "거의 새것",
                                null
                        ),

                        new Post(
                                "소니 A7M4 카메라 + 렌즈 3종",
                                "카메라",
                                "3300만 화소 풀프레임. 24-70mm, 70-200mm, 50mm F1.8 렌즈 포함. 전문 촬영, 유튜브 제작용",
                                70000,
                                "서울 강남구",
                                "양호",
                                null
                        )
                );

                // 1. DB에 저장
                List<Post> savedPosts = postRepository.saveAll(posts);
                System.out.println("✅ MariaDB에 게시글 저장 완료!");

                // 2. RAG 벡터 색인 (MariaDB VECTOR에 저장)
                for (Post post : savedPosts) {
                    ragService.indexPost(post);
                }

                System.out.println("✅ RAG 벡터 색인 완료!");

            } else {
                System.out.println("⚠️ 이미 데이터가 존재합니다. 스킵!");
            }
        };
    }
}